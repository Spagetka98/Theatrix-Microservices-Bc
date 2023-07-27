import React, {
  useState,
  useEffect,
  useImperativeHandle,
  forwardRef,
} from "react";
import styled from "styled-components";
import tw from "twin.macro";

import Star from "../../assets/star";

const StarsPicker = forwardRef(
  ({ ratingCallback, disabled, currentRating }, ref) => {
    const [rating, setRating] = useState(1);
    const [hover, setHover] = useState(null);

    const ratingHandler = (ratingValue) => {
      if (!disabled) {
        setRating(ratingValue);
      }
    };

    useImperativeHandle(
      ref,
      () => ({
        getRating: () => {
          return rating;
        },
      }),
      [rating]
    );

    useEffect(() => {
      if (currentRating) {
        setRating(currentRating);
      }
    }, [currentRating]);

    return (
      <>
        {[...Array(5)].map((star, index) => {
          const ratingValue = 5 - index;
          return (
            <Wrapper key={index}>
              <div
                onMouseEnter={() => setHover(ratingValue)}
                onMouseLeave={() => setHover(null)}
              >
                <Input
                  type="radio"
                  name="rating"
                  onClick={() => ratingHandler(ratingValue)}
                />
                <Star
                  style={{ "--height": "7vh", "--width": "7vh" }}
                  fillColor={
                    ratingValue <= (hover || rating) ? "#ffd930" : "#ffffff"
                  }
                />
              </div>
            </Wrapper>
          );
        })}
      </>
    );
  }
);

const Wrapper = styled.label`
  ${tw`flex-1 pl-2 cursor-pointer flex items-center w-full justify-center `}
`;

const Input = styled.input`
  ${tw`hidden`}
`;
export default StarsPicker;
