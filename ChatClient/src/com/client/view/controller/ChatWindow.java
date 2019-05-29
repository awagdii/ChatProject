/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client.view.controller;

import chatclient.ChatClient;
import com.client.interfaces.ClientInterface;
import com.client.interfaces.impl.ClientImplementation;
import com.client.util.Utilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import static java.lang.System.in;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Ahmed
 */
public class ChatWindow extends AnchorPane {

    @FXML
    private Label label;

    @FXML
    TextArea textArea;

    @FXML
    WebView display;

    @FXML
    TextField editText;

    @FXML
    HTMLEditor editor;
    @FXML
    Button sendBtn;

    @FXML
    Button sendFileBtn;

    @FXML
    Button save;
    @FXML
    ImageView addIcon;
    @FXML
    ImageView saveChat;
    @FXML
    ImageView sendFile;
    @FXML
    ImageView delete;
    @FXML
    MenuItem close;

    String msg;
    String previousMsg = "";

    Image sendAttach = new Image("File:src/img/fileicon.png");
    Image deleteText = new Image("File:src/img/deleteb.png");
    Image saveFile = new Image("File:src/img/saveb.png");
    Image addUser = new Image("File:src/img/addb.png");

    public Vector<ClientInterface> friendObj = null;
    final ChatWindow currChatWindow;
    Stage online;

    Stage myStage;

    public ChatWindow(Vector<ClientInterface> senderObj) {
        friendObj = senderObj;
        currChatWindow = this;
       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/client/view/cfg/chat_window.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    try {  
                        fxmlLoader.load();
                    } catch (IOException ex) {
                        Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        
            
            
           
            Platform.runLater(new Runnable() {

                public void run() {
                    sendBtn.setStyle("-fx-effect: dropshadow( one-pass-box , rgba(0,0,0,0.9) , 1, 0.0 , 0 , 1 ); -fx-background-color: \n"
                            + "#000000,\n"
                            + "linear-gradient(#7ebcea, #2f4b8f),\n"
                            + "linear-gradient(#426ab7, #263e75),\n"
                            + "linear-gradient(#395cab, #223768);\n"
                            + "-fx-background-radius: 30;"
                            + "-fx-background-insets: 0,1,2,3,0;"
                            + "-fx-text-fill: black;"
                            + "-fx-font-weight: bold;"
                            + "-fx-font-size: 14px;"
                            + "-fx-padding: 10 20 10 20;");
                    //textArea.setStyle("-fx-background-radius: 30;");
                    //editText.setStyle("-fx-background-radius: 30;");

                    addIcon.setImage(addUser);
                    delete.setImage(deleteText);
                    sendFile.setImage(sendAttach);
                    saveChat.setImage(saveFile);

                    delete.setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            WebEngine w = display.getEngine();
                            w.loadContent("");
                        }

                    });

                    myStage = new Stage();
                    myStage.setScene(new Scene(ChatWindow.this));
                    myStage.show();
                    myStage.setOnCloseRequest((e) -> {
                        System.out.println("iam out");
                        ClientImplementation.removeFromChatWindows(ChatWindow.this);
                    });

                    addIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent e) {
                            online = new Stage();
                            online.setScene(new Scene(new OnlineFriends(currChatWindow)));
                            online.show();

                        }
                    });
                    sendBtn.setOnAction((e) -> {
                        try {

                            msg = stripHTMLTags(editor.getHtmlText().trim());
                            System.out.println("The msg send is :" + msg);
                            editor.setHtmlText(" ");
                            if (msg.trim().length() != 0) {

                                for (int i = 0; i < friendObj.size(); i++) {
                                    if (friendObj.get(i).haveChatWindow(friendObj)) {
                                        friendObj.get(i).sendMsg(friendObj, Utilities.user.geteMail() + " :" + msg);

                                    } else {
                                        friendObj.get(i).startChatWindow(friendObj);
                                        friendObj.get(i).sendMsg(friendObj, Utilities.user.geteMail() + " :" + msg);
                                    }
                                }
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });

                    sendFile.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            FileChooser fileChooser = new FileChooser();
                            File f1 = fileChooser.showOpenDialog(ChatClient.primaryStage);
                            if (f1 != null) {
                                double totalSize = f1.length();

                                try {
                                    FileInputStream in = new FileInputStream(f1);

                                    for (int i = 0; i < friendObj.size(); i++) {
                                        try {
                                            if (!friendObj.get(i).equals(Utilities.serverInterface.askForConn(Utilities.user.geteMail()))) {

                                                friendObj.get(i).startSendFileWindow(f1, Utilities.user.getFirstName(), Utilities.serverInterface.askForConn(Utilities.user.geteMail()), friendObj.get(i), totalSize);
                                            }
                                        } catch (RemoteException ex) {
                                            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }

                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                                } finally {
                                    try {
                                        in.close();
                                    } catch (IOException ex) {
                                        Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                    });
                    saveChat.setImage(saveFile);
                    saveChat.setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            if (textArea.getLength() != 0) {
                                try {
                                    System.err.println("save");
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    Date dateNow = new Date();
                                    Vector<String> msgs = new Vector<String>();
                                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                                    builderFactory.setValidating(true);

                                    DocumentBuilder builder = builderFactory.newDocumentBuilder();
                                    Document document = builder.newDocument();
                                    Element root = document.createElement("chat");
                                    document.appendChild(root);

                                    SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
                                    Schema schema = schemaFactory.newSchema(new File("src/com/client/view/controller/chatSchema.xsd"));
                                    Validator schemaValidator = schema.newValidator();

                                    Vector<String> lines = new Vector<String>();

                                    String str;

                                    previousMsg = stripHTMLTags(previousMsg);
                                    BufferedReader reader = new BufferedReader(
                                            new StringReader(previousMsg));

                                    while ((str = reader.readLine()) != null) {
                                        lines.add(str);
                                        // for (String l : lines) {

                                        Element message = document.createElement("message");
                                        root.appendChild(message);
                                        Element content = document.createElement("content");
                                        message.appendChild(content);
                                        Attr date = document.createAttribute("date");
                                        message.setAttribute("date", dateFormat.format(dateNow));
                                        // Element text=document.createElement("textMsg");
                                        content.setTextContent(str);

                                        //}
                                        schemaValidator.validate(new DOMSource(document));
                                    }
                                    prettyPrint(document);
                                } catch (ParserConfigurationException ex) {
                                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (SAXException ex) {
                                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }
                        }
                    });
                }
            });
       
    }

    public void prettyPrint(Document xml) {

        try {

            Transformer tf = TransformerFactory.newInstance().newTransformer();

            tf.setOutputProperty(OutputKeys.METHOD, "xml");
            //tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            // transformer.transform(new DOMSource(original), xmlOutput);
            // java.lang.System.out.println(xmlOutput.getWriter().toString());
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter
                    = new FileChooser.ExtensionFilter("xml files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(ChatClient.primaryStage);
            if (file != null) {
                StreamResult result = new StreamResult(file);

                try {
                    tf.transform(new DOMSource(xml), result);
                    transformXSLT(file, new File("src/com/client/view/controller/chatXSL.xsl"));
                } catch (TransformerException ex) {
                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void transformXSLT(File xml, File xslt) throws TransformerConfigurationException {
        // JAXP reads data using the Source interface
        Source xmlSource = new StreamSource(xml);
        Source xsltSource = new StreamSource(xslt);

        // the factory pattern supports different XSLT processors
        TransformerFactory transFact
                = TransformerFactory.newInstance();
        Transformer trans = transFact.newTransformer(xsltSource);
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("html files (*.html)", "*.html");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(ChatClient.primaryStage);
        if (file != null) {
            StreamResult result = new StreamResult(file);

            try {
                trans.transform(xmlSource, result);

            } catch (TransformerException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void displayMsg(String msg, Vector<ClientInterface> sender) {
        friendObj = sender;
        System.out.println("displayMsg");
        Platform.runLater(() -> {

            if (!previousMsg.equals("")) {
                // message.append(System.getProperty("line.separator"));
                previousMsg += "<br>";
                previousMsg += "\r\n";
                previousMsg += msg;
                //message.append(msg);
                display.getEngine().loadContent(previousMsg);
                editor.setHtmlText(" ");
            } else if (previousMsg.equals("")) {
                previousMsg += msg;
                display.getEngine().loadContent(previousMsg);
            }
            editor.setHtmlText(" ");
        });

    }

    private String stripHTMLTags(String htmlText) {

        Pattern pattern = Pattern.compile("<[^>]*>");
        Matcher matcher = pattern.matcher(htmlText);
        final StringBuffer sb = new StringBuffer(htmlText.length());
        while (matcher.find()) {
            matcher.appendReplacement(sb, " ");
        }
        matcher.appendTail(sb);
        System.out.println(sb.toString().trim());
        return sb.toString().replace("&nbsp;", "");

    }

}
