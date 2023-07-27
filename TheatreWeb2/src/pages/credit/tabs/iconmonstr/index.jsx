import React from "react";

import Twitter from "../../../../assets/twitter";
import Facebook from "../../../../assets/facebook";
import Instagram from "../../../../assets/instagram";

import IconBox from "../../iconBox";

const iconmonstr = () => {
  return (
    <>
      <IconBox href="https://iconmonstr.com/twitter-1-svg/">
        <Twitter />
      </IconBox>
      <IconBox href="https://iconmonstr.com/facebook-1-svg/">
        <Facebook />
      </IconBox>
      <IconBox href="https://iconmonstr.com/instagram-11-svg/">
        <Instagram />
      </IconBox>
    </>
  );
};

export default iconmonstr;
