import moment from "moment";

const activityDateFormatter = (activity) => {
  const dateFormat = "DD.MM.yyyy";
  const timeFormat = "HH:mm";

  const startDate = moment(activity?.startDate).format(dateFormat);
  const startTime = moment(activity?.startDate).format(timeFormat);

  let formattedDate = `${startDate} ${startTime}`;

  if (activity?.schemalessData?.end) {
    const endDate = moment(activity.schemalessData.end).format(dateFormat);
    const endTime = moment(activity.schemalessData.end).format(timeFormat);

    if (startDate === endDate) formattedDate += ` - ${endTime}`;
    else formattedDate += ` / ${endDate} ${endTime}`;
  }

  return formattedDate;
};

export default activityDateFormatter;
