package com.saf.framework;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    JSONParser parser = new JSONParser();
    static String json = Paths.get("").toAbsolutePath().toString() + File.separator+ "src" + File.separator + "test" + File.separator + "Elements";

    public List<String> isPageExist(String myPage) {
        List<String> returnValue = new ArrayList<>();
        //index 0 da pageName index 1 de page waitElement
        JSONObject object = null;
        try {
            object = (JSONObject) parser.parse(new FileReader(json + File.separator + myPage.toLowerCase() + ".json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        JSONArray array;
        try {
            array = (JSONArray) object.get("pages");
        } catch (Exception e) {
            array = new JSONArray();
        }


        for (Object o : array) {
            JSONObject page = (JSONObject) o;

            JSONObject pageInfo = (JSONObject) page.get("pageInfo");
            String pagename = (String) pageInfo.get("pageName");
            String waitelement = (String) pageInfo.get("waitelement");
            if (pagename.equalsIgnoreCase(myPage)) {
                returnValue.add(pagename);
                returnValue.add(waitelement);
                System.out.println(pagename + " sayfası bulundu");
                return returnValue;
            }
        }
        return returnValue;
    }

    public String getElement(String myPage, String myElement) {
        JSONObject object = null;
        try {
            object = (JSONObject) parser.parse(new FileReader(json + File.separator + myPage.toLowerCase() + ".json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        JSONArray array = (JSONArray) object.get("pages");
        String value = null;
        String parentName = "";
        for (Object o : array) {
            JSONObject page = (JSONObject) o;

            JSONObject pageInfo = (JSONObject) page.get("pageInfo");
            String pagename = (String) pageInfo.get("pageName");
            parentName = (String) pageInfo.get("parent");
            if (pagename.equalsIgnoreCase(myPage)) {

                JSONArray elements = (JSONArray) page.get("elements");
                for (Object element : elements) {
                    JSONObject elem = (JSONObject) element;
                    value = (String) elem.get(myElement);

                    if (value != null) {
                        break;
                    }
                }

                //control parent
                if (value == null) {
                    try {
                        object = (JSONObject) parser.parse(new FileReader(json + File.separator + parentName + ".json"));
                        array = (JSONArray) object.get("pages");
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                    for (Object obj : array) {
                        JSONObject parentPage = (JSONObject) obj;
                        pageInfo = (JSONObject) parentPage.get("pageInfo");
                        pagename = (String) pageInfo.get("pageName");

                        if (pagename.equalsIgnoreCase(parentName)) {
                            JSONArray parenEelements = (JSONArray) parentPage.get("elements");
                            for (Object element : parenEelements) {
                                JSONObject elem = (JSONObject) element;
                                value = (String) elem.get(myElement);

                                if (value != null) {
                                    break;
                                }
                            }
                            if (value != null) {
                                break;
                            }
                        }
                    }
                }
            }
            if (value != null) {
                break;
            }
        }

        return value;
    }
}
