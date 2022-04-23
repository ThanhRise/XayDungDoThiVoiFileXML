import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EditFileXML {
    // tao noi chua id cau cac lane connection voi nhau
    public static ArrayList<String> output = new ArrayList<String>();

    public static void main(String agrv[]) {

        try {
            // doc file voi DOM parsers
            File file = new File("vd012.net.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            Boolean inputBoolean = true;
            NodeList edgeList = document.getElementsByTagName("edge");
            NodeList connectionList = document.getElementsByTagName("connection");
            try (Scanner scanner = new Scanner(System.in)) {

                System.out.println("lua chon chac nang:");
                System.out.println("1. nhap id lane de tim index va length.");
                System.out.println("2. nhap id lane de in ra do thi vao file output.txt.");
                String choose = scanner.nextLine();
                switch (choose) {
                    case "1":
                        String inputcase = scanner.nextLine();
                        for (int itr = 0; itr < edgeList.getLength(); itr++) {
                            Node edgeNode = edgeList.item(itr);
                            if (edgeNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element element = (Element) edgeNode;
                                String id = element.getAttribute("id");
                                NodeList laneList = edgeList.item(itr).getChildNodes();
                                for (int temp = 0; temp < laneList.getLength(); temp++) {
                                    Node laneNode = laneList.item(temp);
                                    if ("lane".equals(laneNode.getNodeName())) {
                                        String idLane = laneList.item(temp).getAttributes().getNamedItem("id")
                                                .getTextContent()
                                                .trim();
                                        String index = laneList.item(temp).getAttributes().getNamedItem("index")
                                                .getTextContent().trim();
                                        String lenght = laneList.item(temp).getAttributes().getNamedItem("length")
                                                .getTextContent().trim();
                                        if (inputcase.equals(idLane) == true) {
                                            System.out.println("id edge: " + id + ", index cua lane: " + index
                                                    + ", length cua lane: " + lenght);

                                        }
                                    }
                                }
                            }

                        }
                        break;
                    case "2":
                        // tim lane co id == input
                        String input = scanner.nextLine();
                        output.add(input);
                        int count = 0;
                        FileWriter fileWriter = new FileWriter("output.txt");
                        while (inputBoolean == true) {
                            String idInput = null;
                            String indexInput = null;
                            inputBoolean = false;
                            input = output.get(count);
                            fileWriter.write("\r\n");
                            for (int itr = 0; itr < edgeList.getLength(); itr++) {
                                Node edgeNode = edgeList.item(itr);
                                if (edgeNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element element = (Element) edgeNode;
                                    String id = element.getAttribute("id");
                                    NodeList laneList = edgeList.item(itr).getChildNodes();
                                    for (int temp = 0; temp < laneList.getLength(); temp++) {
                                        Node laneNode = laneList.item(temp);
                                        if ("lane".equals(laneNode.getNodeName())) {
                                            String idLane = laneList.item(temp).getAttributes().getNamedItem("id")
                                                    .getTextContent()
                                                    .trim();
                                            String index = laneList.item(temp).getAttributes().getNamedItem("index")
                                                    .getTextContent().trim();
                                            String lenght = laneList.item(temp).getAttributes().getNamedItem("length")
                                                    .getTextContent().trim();
                                            if (output.get(count).equals(idLane) == true) {
                                                fileWriter.write(idLane + " ");
                                                fileWriter.write(lenght + " ");
                                                idInput = id;
                                                indexInput = index;

                                            }
                                        }
                                    }
                                }

                            }
                            // add vao arrayList lane nguoc voi X : -X
                            String output1 = null;
                            int test1 = 0;
                            if (("-".equals(Character.toString(input.charAt(0))) == true)) {
                                String[] input1 = input.split("-");
                                output1 = input1[1];

                            } else if ((input.codePointAt(0) != 45)) {
                                output1 = "-" + input;
                            }
                            fileWriter.write(output1 + " ");
                            for (int i = 0; i < output.size(); i++) {
                                if ((output1.equals(output.get(i)) == true)) {
                                    test1++;
                                }
                            }
                            if (test1 == 0) {

                                output.add(output1);
                            }
                            // tim lane connection voi lane input
                            for (int itr = 0; itr < connectionList.getLength(); itr++) {
                                Node connectionNode = connectionList.item(itr);
                                if (connectionNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element element = (Element) connectionNode;
                                    String from = element.getAttribute("from");
                                    String to = element.getAttribute("to");
                                    String fromlane = element.getAttribute("fromLane");
                                    String tolane = element.getAttribute("toLane");

                                    if ((from.equals(idInput)) && (fromlane.equals(indexInput))) {
                                        input = to + "_" + tolane;
                                        fileWriter.write(input + " ");
                                        int test = 0;
                                        for (int i = 0; i < output.size(); i++) {
                                            if ((input.equals(output.get(i)) == true)) {
                                                test++;
                                            }
                                        }
                                        if (test == 0) {
                                            output.add(input);

                                        }

                                    }
                                }
                            }
                            count++;
                            if (count != output.size() - 1)
                                inputBoolean = true;
                        }
                        fileWriter.close();
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
