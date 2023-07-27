import React, { useState, useEffect } from "react";
import styled from "styled-components";
import tw from "twin.macro";
import { motion } from "framer-motion";

const DivisionBox = ({
  name,
  color,
  addDivisionHandler,
  removeDivisionHandler,
  active,
}) => {
  const [clicked, setClicked] = useState(active);

  useEffect(() => {
    setClicked(active);
  }, [active]);

  const clickHandler = () => {
    setClicked(!clicked);
    if (clicked) {
      removeDivisionHandler(name);
    } else {
      addDivisionHandler(name);
    }
  };

  return (
    <Box
      onClick={clickHandler}
      clicked={clicked ? 1 : 0}
      color={color}
      style={{ "--bg": color }}
      whileHover={{ scale: 0.97 }}
    >
      {name}
    </Box>
  );
};

const Box = styled(motion.div).attrs({ className: "border-slate-300" })(
  ({ clicked }) => [
    tw`p-2 rounded-2xl min-w-min my-2 whitespace-nowrap text-center mx-3 font-extralight cursor-pointer select-none text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all`,
    clicked && tw`bg-[var(--bg,"#f3aae9")] shadow-lg`,
  ]
);
export default DivisionBox;
