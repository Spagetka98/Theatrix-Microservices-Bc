import React from "react";

import FreepikLinks from "../../../../assets/freepik";
import ImgBox from "../../imgBox";

const freepik = ({ scrollPosition }) => {
  return (
    <>
      {FreepikLinks.map((splash, index) => {
        return (
          <ImgBox
            key={index}
            img={splash.imgUrl}
            href={splash.creatorUrl}
            scrollPosition={scrollPosition}
          />
        );
      })}
    </>
  );
};

export default freepik;
