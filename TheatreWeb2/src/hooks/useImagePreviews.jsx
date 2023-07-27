import { useState, useEffect } from "react";

import _ from "lodash";

import Previews from "../assets/unsplashLinks";

const useImagePreviews = (activities) => {
  const [images, setImages] = useState([]);

  useEffect(() => {
    if (activities == null) return;
    setImages(_.shuffle(Previews));
  }, [activities]);

  return images;
};

export default useImagePreviews;
