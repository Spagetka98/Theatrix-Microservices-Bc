package cz.osu.activityservice.dataLoader;

import cz.osu.activityservice.model.enums.EDateFormats;
import cz.osu.activityservice.model.enums.ETheatreActivity;
import cz.osu.activityservice.model.enums.ETheatreActivitySchemaless;
import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.model.enums.ETimeZones;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cz.osu.activityservice.utility.DateUtility.changeDateFormat;

@Slf4j
public class XmlLoader {
    public static final String NDM_URI = "https://www.ndm.cz/cz/program/xml";
    private static final String ROOT_NODE = "Item";
    private final NodeList nodeList;
    public XmlLoader() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(NDM_URI).openStream());

        doc.getDocumentElement().normalize();
        nodeList = doc.getElementsByTagName(ROOT_NODE);
    }


    public List<TheatreActivity> getTheatreActivity() {
        if (nodeList == null) throw new NullPointerException("Value of nodeList in class XmlLoader is null");

        List<TheatreActivity> theatreActivities = new ArrayList<>();
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                Optional<TheatreActivity> activity = createActivity(element);

                activity.ifPresent(theatreActivities::add);
            }
        }

        return theatreActivities;
    }

    private Optional<TheatreActivity> createActivity(Element element) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EDateFormats.DD_MM_YYYY_HH_MM_SS_HYPHEN.getValue());

            String activity_startDate = changeDateFormat(element.getElementsByTagName(ETheatreActivity.DATE.getAttribute()).item(0).getTextContent(), EDateFormats.YYYY_MM_DD_HYPHEN, EDateFormats.DD_MM_YYYY_HYPHEN);

            String activity_startTime = element.getElementsByTagName(ETheatreActivity.START.getAttribute()).item(0).getTextContent();

            long parsed_id = Long.parseLong(element.getElementsByTagName(ETheatreActivity.ID.getAttribute()).item(0).getTextContent());

            Instant startDate = LocalDateTime.parse(String.format("%s %s", activity_startDate, activity_startTime), formatter)
                    .atZone(ZoneId.of(ETimeZones.EUROPE_PRAGUE.getValue()))
                    .toInstant();

            String activity_stage = element.getElementsByTagName(ETheatreActivity.STAGE.getAttribute()).item(0).getTextContent();

            String activity_division = element.getElementsByTagName(ETheatreActivity.DIVISION.getAttribute()).item(0).getTextContent();

            String activity_name = checkActivityName(element.getElementsByTagName(ETheatreActivity.NAME.getAttribute()).item(0).getTextContent(),
                    activity_division,activity_stage);

            TheatreActivity theatreActivity = new TheatreActivity(parsed_id, activity_name, activity_stage, activity_division,
                    startDate, false
            );

            List<String> schemalessData = new ArrayList<>(List.of(
                    ETheatreActivitySchemaless.AUTHOR.getAttribute(),
                    ETheatreActivitySchemaless.URL.getAttribute()
            ));

            schemalessData.forEach((node) -> addSchemalessData(element, theatreActivity, node));

            addEndDate(element, activity_startDate, theatreActivity, formatter);
            addDescription(element, theatreActivity);

            return Optional.of(theatreActivity);
        } catch (Exception exception) {
            log.error(String.format("Error during saving TheatreActivity with id %s in XmlLoader.createActivity !",
                    Long.valueOf(element.getElementsByTagName(ETheatreActivity.ID.getAttribute()).item(0).getTextContent())));
        }

        return Optional.empty();
    }

    private String checkActivityName(String potentialName, String division, String stage) {
        if (potentialName == null || potentialName.isEmpty())
            return String.format("%s %s",division,stage).trim();
        else
            return potentialName;
    }

    private void addSchemalessData(Element element, TheatreActivity theatreActivity, String node_title) {
        String attribute = element.getElementsByTagName(node_title).item(0).getTextContent();

        if (attribute != null && !attribute.trim().isEmpty()) {
            theatreActivity.setSchemalessData(node_title, attribute);
        }
    }

    private void addEndDate(Element element, String startDate, TheatreActivity theatreActivity, DateTimeFormatter formatter) {
        String endTime_attribute = element.getElementsByTagName(ETheatreActivitySchemaless.END.getAttribute()).item(0).getTextContent();

        if (endTime_attribute != null && !endTime_attribute.trim().isEmpty()) {
            Instant endDate = LocalDateTime.parse(String.format("%s %s", startDate, endTime_attribute), formatter)
                    .atZone(ZoneId.of(ETimeZones.EUROPE_PRAGUE.getValue()))
                    .toInstant();
            theatreActivity.setSchemalessData(ETheatreActivitySchemaless.END.getAttribute(), endDate);
        }
    }

    private void addDescription(Element element, TheatreActivity theatreActivity) {
        String attribute = element.getElementsByTagName(ETheatreActivitySchemaless.DESCRIPTION.getAttribute()).item(0).getTextContent();
        String KEY = ETheatreActivitySchemaless.DESCRIPTION.getAttribute();

        if (attribute != null && !attribute.trim().isEmpty()) {
            theatreActivity.setSchemalessData(KEY, attribute
                    .replaceAll("Mediální partneři:", "")
                    .replaceAll("Partneři:", "")
                    .replaceAll("(?m)^[ \t]*\r?\n", ""));
        }
    }
}