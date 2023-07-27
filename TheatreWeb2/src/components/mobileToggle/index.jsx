import React, { useEffect } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { useMediaQuery } from "react-responsive";

import Mobile from "../../assets/mobile";
import {
  setItemLocalStorage,
  getItemLocalStorage,
} from "../../config/localStorage/services";
import { MOBILE_MODE } from "../../config/localStorage/items";
import { TABLET } from "../../config/sizes";

const MobileToggle = ({ status, mobileModeHandler }) => {
  const isMobile = useMediaQuery({
    query: TABLET,
  });

  useEffect(() => {
    const mobileMode = getItemLocalStorage(MOBILE_MODE);

    if (mobileMode) {
      mobileModeHandler(mobileMode);
    } else {
      mobileModeHandler(isMobile);
    }
  }, [mobileModeHandler, isMobile]);

  const clickHandler = () => {
    mobileModeHandler(!status);
    setItemLocalStorage(MOBILE_MODE, !status);
  };

  return (
    <Wrapper>
      <Button
        onClick={clickHandler}
        style={{ "--bgColor": status ? "#a7ff71bc" : "#ffffffa1" }}
      >
        <Mobile />
      </Button>
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex z-50 fixed bottom-0 right-0 w-[5.5vw] h-auto min-w-[60px] m-2`}
`;

const Button = styled.button.attrs({ className: "bg-slate-50" })`
  ${tw` w-full h-full p-2 border-2 xl:border-4 4xl:border-8 border-gray-300 hover:border-purple-500 shadow-lg bg-[var(--bgColor,"#b7b7b7")] rounded-lg font-semibold min-w-[50px]`}
`;

export default MobileToggle;
