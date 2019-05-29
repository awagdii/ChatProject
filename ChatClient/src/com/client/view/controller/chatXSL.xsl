<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <body>
               
                <table align="center" border="1">
                    <caption>My Chat</caption>
                    <tr> 
                        <td> 
                            <xsl:for-each select="chat/message">  
                          
                                
                                
                                <pre>
                                    <h2>
                                        <xsl:value-of select="content  "/>
                                   
                                        <font size="2" color="blue">   Sent At  </font>
                                    
                                        <xsl:value-of select="//@date"/> 
                                    </h2>
                                </pre>
                            
                            </xsl:for-each>
                        </td> 
                    </tr> 
                
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>

