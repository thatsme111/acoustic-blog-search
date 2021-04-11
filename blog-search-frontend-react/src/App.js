import { Container, TextField, InputAdornment, Button, Avatar, List, ListItem, ListItemText, Typography, ListItemAvatar } from '@material-ui/core';
import { Fragment, useState } from 'react';
import { Autocomplete, Pagination } from '@material-ui/lab';
import SearchIcon from '@material-ui/icons/Search';
import axios from "axios";
import './App.css';

const debounce = (function () {
  let timeout;
  return function (func, delay) {
    clearTimeout(timeout);
    timeout = setTimeout(func, delay);
  };
})();

export default function App() {
  const [search, setSearch] = useState("");
  const [autoCompleteOptions, setAutoCompleteOptions] = useState([]);
  const [searchDisabled, setSearchDisabled] = useState(false);
  const [searchResult, setSearchResult] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const handleTextFieldOnInput = async e => {
    const value = e.target.value;
    setSearch(value);
    let options = [];
    if (value) {
      const response = await axios.get("http://localhost:8080/autofill?q=" + value);
      options = response.data;
    }
    setAutoCompleteOptions(options);
  };

  const handleSearchButtonClicked = async () => {
    setSearchDisabled(true);
    const response = await axios.get("http://localhost:8080/blogs?q=" + search + "&page=" + page);
    const { list, totalPages } = response.data;
    setTotalPages(totalPages);
    list.forEach(e => {
      e.content = e.content
        .split(new RegExp(search, "i"))
        .join(`<span class="highlight">${search}</span>`);
    });
    setSearchResult(list);
    setSearchDisabled(false);
  };

  const handleAutoCompleteInputChange = e => {
    const value = e.target.innerHTML;
    setSearch(e.target.innerHTML);
    if (value) {
      handleSearchButtonClicked();
    }
  };

  const handlePaginationChange = (e, currPage) => {
    setPage(currPage);
    handleSearchButtonClicked();
  };

  let pagination = "";
  if (totalPages) {
    pagination = <Pagination 
        count={totalPages} 
        variant="outlined" 
        shape="rounded"
        onChange={handlePaginationChange}
        style={{ marginBottom: 48 }} />;
  }

  return (
    <div className="App">
      <Container>
        <Autocomplete
          id="search-autocomplete"
          noOptionsText="Did not match any blogs."
          options={autoCompleteOptions}
          onInputChange={handleAutoCompleteInputChange}
          style={{ marginTop: 24 }}
          clearOnBlur={false}
          renderInput={(params) =>
            <TextField
              {...params}
              onInput={e => debounce(() => handleTextFieldOnInput(e), 200)}
              placeholder="Acoustic Blogs Search"
              variant="outlined"
              InputProps={{
                ...params.InputProps,
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon />
                  </InputAdornment>
                ),
                endAdornment: (
                  <InputAdornment position="end">
                    <Button
                      variant="contained"
                      color="primary"
                      disabled={searchDisabled}
                      onClick={handleSearchButtonClicked}
                    >Search</Button>
                  </InputAdornment>
                ),
              }}
            />
          }
        />
        <List className="result-list">
          {
            searchResult.map((blog, index) => {
              return <ListItem alignItems="flex-start" key={index}>
                <ListItemAvatar>
                  <>
                    <Avatar alt="Author" style={{ width: 100, height: 100 }} src={blog.authorImageUrl} />
                    <Typography variant="body1" color="textPrimary">{blog.authorName}</Typography>
                    <Typography variant="caption" color="textPrimary">{blog.authorJob}</Typography>
                  </>
                </ListItemAvatar>
                <ListItemText>
                  <>
                    <a href={blog.url}>
                      <Typography variant="h6">
                        <div dangerouslySetInnerHTML={{ __html: blog.title }}></div>
                      </Typography>
                    </a>
                    <div><Typography variant="caption">{blog.date}</Typography></div>
                    <Typography component="div" variant="body2">
                      <div dangerouslySetInnerHTML={{ __html: blog.content }}></div>
                    </Typography>
                  </>
                </ListItemText>
              </ListItem>
            })
          }
        </List>
        {pagination}
      </Container>
    </div>
  );
};
