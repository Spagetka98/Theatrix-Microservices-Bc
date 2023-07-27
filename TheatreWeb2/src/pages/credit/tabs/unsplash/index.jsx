import React from "react";

import UnsplashLinks from "../../../../assets/unsplashLinks";
import ImgBox from "../../imgBox";

const unsplash = ({ scrollPosition }) => {
  return (
    <>
      {UnsplashLinks.map((preview, index) => {
        return (
          <ImgBox
            key={index}
            img={preview.imgUrl}
            href={preview.creatorUrl}
            scrollPosition={scrollPosition}
            effect="blur"
          />
        );
      })}
    </>
  );
};

export default unsplash;
