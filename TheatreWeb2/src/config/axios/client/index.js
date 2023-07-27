import axios from "axios";

const Client = axios.create({
  //baseURL: "https://api.spagetka.xyz/",
  baseURL: "http://localhost:9191/",
  headers: {
    "Content-Type": "application/json",
  },
});

export default Client;
