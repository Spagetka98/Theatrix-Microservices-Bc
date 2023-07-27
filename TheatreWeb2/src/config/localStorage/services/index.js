export const getItemLocalStorage = (name) => {
  return JSON.parse(localStorage.getItem(name));
};

export const setItemLocalStorage = (name, data) => {
  localStorage.setItem(name, JSON.stringify(data));
};

export const removeItemLocalStorage = (name) => {
  localStorage.removeItem(name);
};
