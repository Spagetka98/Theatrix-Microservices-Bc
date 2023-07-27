import React from "react";

import OSU from "../../../../assets/osuLogo";
import ImgBox from "../../imgBox";

const Osu = ({ scrollPosition }) => {
  return (
    <ImgBox
      img={OSU.imgUrl}
      href={OSU.creatorUrl}
      scrollPosition={scrollPosition}
    />
  );
};

export default Osu;
