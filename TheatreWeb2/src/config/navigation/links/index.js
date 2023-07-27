import { CONFIG_LINKS } from "../../texts";
import {
  ALL_ACTIVITIES,
  LIKED_ACTIVITIES,
  DISLIKED_ACTIVITIES,
  RATED_ACTIVITIES,
  PASSWORD_CHANGE,
} from "../paths";

const NavigationLinks = {
  ActivitiesLinks: {
    title: CONFIG_LINKS.activities.title,
    links: [
      {
        label: CONFIG_LINKS.activities.subLinks.all,
        to: ALL_ACTIVITIES,
      },
      {
        label: CONFIG_LINKS.activities.subLinks.liked,
        to: LIKED_ACTIVITIES,
      },
      {
        label: CONFIG_LINKS.activities.subLinks.disliked,
        to: DISLIKED_ACTIVITIES,
      },
      {
        label: CONFIG_LINKS.activities.subLinks.rated,
        to: RATED_ACTIVITIES,
      },
    ],
  },

  SettingLinks: {
    title: CONFIG_LINKS.settings.title,
    links: [
      {
        label: CONFIG_LINKS.settings.subLinks.passwordChange,
        to: PASSWORD_CHANGE,
      },
    ],
  },
};

export default NavigationLinks;
