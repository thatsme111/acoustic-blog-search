import { Container, TextField, InputAdornment, Button } from '@material-ui/core';
import { useState } from 'react';
import { Autocomplete } from '@material-ui/lab';
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

  const handleSearchButtonClicked = () => {
    console.log("searching...", search);
  };

  return (
    <div className="App">
      <Container>
        <Autocomplete
          id="search-autocomplete"
          className={search ? 'empty-search' : ''}
          noOptionsText="Did not match any blogs."
          options={autoCompleteOptions}
          onInputChange={e => setSearch(e.target.innerHTML)}
          style={{ marginTop: 24 }}
          renderInput={(params) =>
            <TextField
              {...params}
              value={search}
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
                      onClick={handleSearchButtonClicked}
                    >Search</Button>
                  </InputAdornment>
                ),
              }}
            />
          }
        />
      </Container>
    </div>
  );
};
