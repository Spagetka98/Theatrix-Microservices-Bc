import React from "react";
import styled from "styled-components";
import tw from "twin.macro";
import moment from "moment";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";

import PreviewDetailsTitle from "../previewDetailsTitle";

import FlaticonLinks from "../../assets/flaticon";

import Colors from "../../config/colors/ratingButton";
import { COMP_RATING_PREVIEWS } from "../../config/texts";
import { USER_REVIEWS } from "../../config/navigation/paths";

const RatingPreviews = ({ data, previewDetails }) => {
  return (
    <Wrapper>
      {data?.map((reviews, index) => {
        return (
          <Card key={index}>
            <Header>
              {previewDetails && (
                <PreviewDetailsTitle
                  loading={previewDetails?.loading}
                  error={previewDetails?.error}
                  data={previewDetails?.data}
                  review={reviews}
                />
              )}
              <HeaderBar>
                <Title>{reviews.title}</Title>
                <RatingContainer>
                  <Rating style={{ "--textColor": Colors[reviews.rating - 1] }}>
                    {reviews.rating}/{COMP_RATING_PREVIEWS.totalRating}
                  </Rating>
                </RatingContainer>
              </HeaderBar>
            </Header>
            <Body>{reviews.text}</Body>
            <Footer>
              <UpperContainer>
                <Img src={FlaticonLinks[0].imgUrl} alt="profilePic" />

                <Link to={USER_REVIEWS + "/" + reviews.username}>
                  <Username whileHover={{ scale: 0.97 }}>
                    {reviews.username}
                  </Username>
                </Link>
              </UpperContainer>

              <LowerContainer>
                <DateCreation>
                  {COMP_RATING_PREVIEWS.dateCreation}
                  {moment(reviews.creation).format("DD.MM.yyyy HH:mm")}
                </DateCreation>
                {reviews.modified !== reviews.creation && (
                  <DateModified>
                    {COMP_RATING_PREVIEWS.dateModified}
                    {moment(reviews.modified).format("DD.MM.yyyy HH:mm")}
                  </DateModified>
                )}
              </LowerContainer>
            </Footer>
          </Card>
        );
      })}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  ${tw`flex flex-col min-h-screen items-center text-sm md:text-base lg:text-lg 2xl:text-xl 3xl:text-3xl 4xl:text-4xl transition-all`}
`;

const Card = styled.div.attrs({ className: "h-fit bg-slate-50" })`
  ${tw`flex flex-col w-[85%] min-h-[300px] max-h-[600px] xl:min-h-[500px] xl:max-h-[800px] 3xl:min-h-[1000px] 3xl:max-h-[800px] border-2 lg:border-4 3xl:border-8 hover:border-purple-500 rounded-2xl shadow-xl mb-8 text-base lg:text-xl 2xl:text-2xl 3xl:text-4xl 4xl:text-5xl transition-all`}
`;

const Header = styled.div`
  ${tw`flex flex-1 flex-col`}
`;

const HeaderBar = styled.div`
  ${tw`flex flex-1 flex-col lg:flex-row`}
`;

const Body = styled.div.attrs({ className: "flex-[8_8_0%]" })`
  ${tw`bg-white m-2 p-2 border-2 rounded-2xl min-h-[150px] lg:min-h-[80%] break-all overflow-auto`}
`;

const Footer = styled.div`
  ${tw`flex flex-col flex-1 lg:flex-row m-2 italic text-sm lg:text-base 2xl:text-lg 3xl:text-2xl 4xl:text-3xl transition-all`}
`;

const Title = styled.div.attrs({
  className:
    "flex-[4_4_0%] bg-gradient-to-r from-pink-500 to-violet-500 bg-clip-text text-transparent",
})`
  ${tw`flex items-center break-all px-2 py-2 font-extrabold justify-center lg:justify-start `}
`;

const RatingContainer = styled.div`
  ${tw`flex-1 flex justify-center items-center`}
`;

const Rating = styled.div`
  ${tw`font-extrabold rounded-full p-3 text-[var(--textColor,"#282828")]`}
`;

const UpperContainer = styled.div`
  ${tw`flex flex-1 items-center pb-2`}
`;

const LowerContainer = styled.div`
  ${tw`flex flex-col flex-1 mr-2`}
`;

const Img = styled.img`
  ${tw`max-w-[10%] object-contain min-w-[50px]`}
`;

const Username = styled(motion.button)`
  ${tw`flex flex-col ml-2 break-all items-end`}
`;

const DateCreation = styled.div`
  ${tw`flex-1 flex justify-center lg:justify-end items-center break-all`}
`;

const DateModified = styled.div`
  ${tw`flex-1 flex justify-center lg:justify-end items-center break-all`}
`;
export default RatingPreviews;
