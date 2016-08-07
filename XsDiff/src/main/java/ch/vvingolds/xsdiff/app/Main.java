/*
  This file is licensed to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package ch.vvingolds.xsdiff.app;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** entry point */
public class Main {

    protected static final String TESTDATA_FOLDER = "../testdata/";

    public static void main( final String[] args ) {
        new App().run();
    }

    /** app bootstrap */
    public static class App {

        private XmlDomUtils xmlDomUtils = new XmlDomUtils();

        public void run() {
            try {
                runDiff( TESTDATA_FOLDER, "simple-schema1.xsd", TESTDATA_FOLDER, "simple-schema2.xsd" );
            }
            catch( Exception e ) {
                System.out.println("Error, failed to run, exception occurred: " + e );
                e.printStackTrace();
            }
        }

        public void runDiff( String folder1, String file1, String folder2, String file2 ) throws Exception {
            FileSystem fs = FileSystems.getDefault();
            Path f1 = fs.getPath( folder1, file1 );
            Path f2 = fs.getPath( folder2, file2 );
            System.out.println( "comparing: "+f1+" with "+f2 );
            runDiff( Files.newBufferedReader( f1 ),
                    Files.newBufferedReader( f2 ) );
        }

        public void runDiff( final Reader file1, final Reader file2 ) {

            try {
                DocumentBuilder docBuilder = xmlDomUtils.documentBuilder();
                Document controlDoc = docBuilder.parse( new InputSource( file1 ) );
                Document testDoc = docBuilder.parse( new InputSource( file2 ) );

                new XmlSchemaDiffReport().runDiff( controlDoc, testDoc );
            }
            catch( ParserConfigurationException e ) {
                throw new RuntimeException( "Failed to parse: ", e );
            }
            catch( SAXException e ) {
                throw new RuntimeException( "Failed to parse: ", e );
            }
            catch( IOException e ) {
                throw new RuntimeException( "Failed to parse: ", e );
            }
        }
    }
}
