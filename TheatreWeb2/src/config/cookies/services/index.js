import Cookies from "universal-cookie";

const cookies = new Cookies();

export const getItemCookies = (name) => {
  return cookies.get(name);
};

export const setItemCookies = (name, data) => {
  cookies.set(name, data, { path: "/", secure: true, sameSite: "none" });
};

export const removeItemCookies = (name) => {
  cookies.remove(name);
};
