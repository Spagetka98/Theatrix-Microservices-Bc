import { HELPERS_FUNC_REGISTRATION_VALIDATION } from "../../config/texts";

export const usernameValidation = (username) => {
  let usernameError = undefined;

  if (!username) {
    usernameError =
      HELPERS_FUNC_REGISTRATION_VALIDATION.usernameValidation.nameEmpty;
  } else if (username.replace(/\s+/g, "") !== username) {
    usernameError =
      HELPERS_FUNC_REGISTRATION_VALIDATION.usernameValidation.nameWhiteSpaces;
  } else if (username.length < 5) {
    usernameError =
      HELPERS_FUNC_REGISTRATION_VALIDATION.usernameValidation.nameShort;
  } else if (username.length > 25) {
    usernameError =
      HELPERS_FUNC_REGISTRATION_VALIDATION.usernameValidation.nameLong;
  } else if (!/^[a-zA-Z]+$/.test(username)) {
    usernameError =
      HELPERS_FUNC_REGISTRATION_VALIDATION.usernameValidation.nameInvalidChars;
  }

  return usernameError;
};

export const emailValidation = (email) => {
  let emailError = undefined;

  if (!email) {
    emailError =
      HELPERS_FUNC_REGISTRATION_VALIDATION.emailValidation.emailEmpty;
  } else if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(email)) {
    emailError =
      HELPERS_FUNC_REGISTRATION_VALIDATION.emailValidation.emailInvalidChars;
  }

  return emailError;
};

export const passValidation = (newPass, correctPass) => {
  let passError = undefined;

  if (!newPass) {
    passError =
      HELPERS_FUNC_REGISTRATION_VALIDATION.passwordValidation.passEmpty;
  } else if (newPass.replace(/\s+/g, "") !== newPass) {
    passError =
      HELPERS_FUNC_REGISTRATION_VALIDATION.passwordValidation.passWhiteSpaces;
  } else if (!/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,45}$/.test(newPass)) {
    passError =
      HELPERS_FUNC_REGISTRATION_VALIDATION.passwordValidation.passInvalid;
  } else if (newPass !== correctPass) {
    passError =
      HELPERS_FUNC_REGISTRATION_VALIDATION.passwordValidation.passDifferent;
  }

  return passError;
};
