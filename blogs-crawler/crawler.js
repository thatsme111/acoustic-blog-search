const axios = require("axios");
const puppeteer = require('puppeteer');
const xml2js = require("xml2js");
const fs = require("fs");

const getAllBlogsUrls = async () => {
    const sitemap = "https://acoustic.com/sitemap.xml";
    const response = await axios.get(sitemap);
    const parsedSiteMap = await xml2js.parseStringPromise(response.data);
    return parsedSiteMap.urlset.url
        .map(e => e.loc[0])
        .filter(e => e.includes("/blog/"));
};

const parseBlog = async (page, url) => {
    await page.goto(url);
    await page.waitForSelector('.gradient-hero__title', { timeout: 1000 });
    return page.evaluate(() => {
        const check = e => e ? e : { children: [], getAttribute: () => { } };
        return {
            url: document.location.href,
            title: check(document.querySelector('.gradient-hero__title')).innerHTML,
            date: check(document.querySelector('.resource-meta-info__date')).innerHTML,
            author: {
                name: check(document.querySelector('.resource-meta-info__label')).innerHTML,
                job: check(document.querySelector('.contributor-info__job')).innerHTML,
                imageUrl: check(document.querySelector('.contributor-info__headshot > picture > img')).getAttribute("data-src"),
            },
            content: Array.from(check(document.querySelector(".basic-text__inner")).children).map(e => e.textContent)
        }
    });
}

(async () => {
    const padIndex = index => (index < 10 ? '0' : '') + index;
    try {
        const browser = await puppeteer.launch();
        const page = await browser.newPage();
        const urls = await getAllBlogsUrls();
        for ([index, url] of urls.entries()) {
            console.log(padIndex(index), url);
            const blog = await parseBlog(page, url);
            const fileName = `blogs/${padIndex(index)} ${url.substr(url.indexOf("blog/") + "blog/".length)}.json`;
            fs.writeFileSync(fileName, JSON.stringify(blog, null, 4));
        }
        await browser.close();
    } catch (error) {
        console.log(error);
    }
})();


// fs.readdir("blogs", (err, files) => {
//     files.forEach(file => {
//         console.log(file);
//         fs.renameSync("blogs/" + file, "blogs/" + file.substr(0,2) + ".json");
//         // const fileName = "blogs/" + file;
//         // const content = fs.readFileSync(fileName).toString();
//         // fs.writeFileSync("blogs/" + file, JSON.stringify(JSON.parse(content), null, 4))
//     });
// });