import Layout from "./layout/index";
import { Routes, Route } from "react-router-dom";

import LandingPage from "./pages/landing";
import RegistrationPage from "./pages/registration";
import LoginPage from "./pages/login";
import CreditPage from "./pages/credit";
import ErrorPage from "./pages/error";
import AllActivitiesPage from "./pages/allActivities";
import LikedActivitiesPage from "./pages/likedActivities";
import DislikedActivitiesPage from "./pages/dislikedActivities";
import RatedActivitiesPage from "./pages/ratedActivities";
import ActivityDetails from "./pages/activityDetails";
import ActivityRatings from "./pages/activityRatings";
import PasswordChange from "./pages/passwordChange";
import UserReviews from "./pages/userReviews";

import {
  HOME,
  ALL_ACTIVITIES,
  LIKED_ACTIVITIES,
  DISLIKED_ACTIVITIES,
  RATED_ACTIVITIES,
  PASSWORD_CHANGE,
  DETAILS,
  ACITIVITY_RATINGS,
  LOGIN,
  REGISTRATION,
  CREDIT,
  NOT_FOUND,
  USER_REVIEWS,
} from "./config/navigation/paths";

const App = () => {
  return (
    <Layout>
      <Routes>
        <Route path={HOME} element={<LandingPage />} />

        <Route path={ALL_ACTIVITIES} element={<AllActivitiesPage />} />

        <Route path={LIKED_ACTIVITIES} element={<LikedActivitiesPage />} />

        <Route
          path={DISLIKED_ACTIVITIES}
          element={<DislikedActivitiesPage />}
        />

        <Route path={RATED_ACTIVITIES} element={<RatedActivitiesPage />} />
        <Route path={PASSWORD_CHANGE} element={<PasswordChange />} />
        <Route path={DETAILS + "/:id"} element={<ActivityDetails />} />
        <Route
          path={ACITIVITY_RATINGS + "/:id"}
          element={<ActivityRatings />}
        />
        <Route path={USER_REVIEWS + "/:username"} element={<UserReviews />} />
        <Route path={LOGIN} element={<LoginPage />} />
        <Route path={REGISTRATION} element={<RegistrationPage />} />
        <Route path={CREDIT} element={<CreditPage />} />
        <Route path={NOT_FOUND} element={<ErrorPage />} />
      </Routes>
    </Layout>
  );
};

export default App;
