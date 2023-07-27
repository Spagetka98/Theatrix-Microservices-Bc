import React from "react";

import FlaticonLinks from "../../../../assets/flaticon";
import ImgBox from "../../imgBox";

const flaticon = ({ scrollPosition }) => {
  return (
    <ImgBox
      img={FlaticonLinks[0].imgUrl}
      href={FlaticonLinks[0].creatorUrl}
      scrollPosition={scrollPosition}
    />
  );
};

export default flaticon;
