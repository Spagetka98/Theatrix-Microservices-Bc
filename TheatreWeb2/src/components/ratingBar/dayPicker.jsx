import React, { useState, useRef, useEffect } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import moment from "moment";
import { useMediaQuery } from "react-responsive";

import useOutsideClick from "../../hooks/useOutsideClick";

import { SMALL, MOBILE, TABLET, XL } from "../../config/sizes";
import { COMP_RATING_BAR_DAY_PICKER } from "../../config/texts";

import { DayPicker } from "react-day-picker";
import "react-day-picker/dist/style.css";
import cs from "date-fns/locale/cs";

import Actions from "./actions";

const DayPickerBox = ({
  plusDays = 0,
  state,
  active,
  action,
  title,
  dispatch,
}) => {
  const [date, setDate] = useState(
    state
      ? moment(state, "DD-MM-YYYY")
      : new Date().setDate(new Date().getDate() + plusDays)
  );
  const boxRef = useRef(null);
  const outsideClick = useOutsideClick(boxRef);
  const isSmall = useMediaQuery({
    query: SMALL,
  });
  const isMobile = useMediaQuery({
    query: MOBILE,
  });
  const isTablet = useMediaQuery({
    query: TABLET,
  });
  const isXL = useMediaQuery({
    query: XL,
  });

  useEffect(() => {
    dispatch({ type: action, payload: date });
    dispatch({ type: Actions.SET_PAGE, payload: 1 });
  }, [date, action, dispatch]);

  return (
    <Wrapper active={active}>
      <Title>{COMP_RATING_BAR_DAY_PICKER.title + "" + title}</Title>
      <Box ref={boxRef}>
        <DateBox clicked={outsideClick}>
          {moment(date).format("DD.MM.YYYY")}
        </DateBox>

        <PickerWrapper clicked={outsideClick}>
          <Container>
            <Picker
              selected={date}
              onSelect={setDate}
              style={{
                "--rdp-cell-size": isSmall
                  ? "10vw"
                  : isMobile
                  ? "8.5vw"
                  : isTablet
                  ? "7.5vw"
                  : isXL
                  ? "3.8vw"
                  : "1.5vw",

                "--rdp-background-color": "#ffe7ee",
                "--rdp-accent-color": "#feb1c7",
                "--rdp-outline": "none",
                "--rdp-outline-selected": "#ffe7ee",
              }}
              mode="single"
              locale={cs}
            />
          </Container>
        </PickerWrapper>
      </Box>
    </Wrapper>
  );
};

const Wrapper = styled.div(({ active }) => [
  tw`relative py-2 select-none`,
  !active && tw`pointer-events-none opacity-40`,
]);

const Title = styled.div`
  ${tw`px-20 w-full lg:w-auto my-2 3xl:my-6 font-semibold text-center text-base md:text-lg lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-6xl transition-all`}
`;

const Picker = styled(DayPicker)`
  ${tw`p-1 bg-white border-2 rounded-xl border-purple-400 shadow-md z-10 mt-0.5`}
`;

const DateBox = styled.div.attrs({ className: "border-slate-300" })(
  ({ clicked }) => [
    tw`text-center bg-white border-2 rounded-xl py-1 cursor-pointer`,
    clicked === undefined
      ? tw``
      : !clicked
      ? tw`border-purple-400 shadow-md`
      : tw``,
  ]
);

const Container = styled.div`
  ${tw`flex justify-center`}
`;

const PickerWrapper = styled.div(({ clicked }) => [
  tw`absolute`,
  clicked === undefined ? tw`hidden` : !clicked ? tw`` : tw`hidden`,
]);

const Box = styled.div`
  ${tw`font-light text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-2xl 4xl:text-3xl transition-all`}
`;
export default DayPickerBox;
