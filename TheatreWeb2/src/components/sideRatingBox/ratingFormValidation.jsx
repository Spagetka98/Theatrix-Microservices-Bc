import { COMP_SIDE_RATING_BOX_RATING_FORM_VALIDATION } from "../../config/texts";

export const titleValidation = (title) => {
  const validatedTitle = title.trim().replace(/\s/g, " ");

  if (validatedTitle.length <= 0) {
    return COMP_SIDE_RATING_BOX_RATING_FORM_VALIDATION.emptyTitle;
  }

  if (validatedTitle.length > 100) {
    return COMP_SIDE_RATING_BOX_RATING_FORM_VALIDATION.longTitle;
  }
  return null;
};

export const textValidation = (text) => {
  const validatedText = text.trim().replace(/\s/g, " ");

  if (validatedText.length <= 0) {
    return COMP_SIDE_RATING_BOX_RATING_FORM_VALIDATION.emptyText;
  }

  if (validatedText.length > 5000) {
    return COMP_SIDE_RATING_BOX_RATING_FORM_VALIDATION.longText;
  }
  return null;
};
