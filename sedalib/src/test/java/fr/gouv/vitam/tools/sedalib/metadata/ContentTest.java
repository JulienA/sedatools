package fr.gouv.vitam.tools.sedalib.metadata;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import fr.gouv.vitam.tools.sedalib.metadata.content.*;
import fr.gouv.vitam.tools.sedalib.metadata.namedtype.*;
import org.junit.jupiter.api.Test;

import fr.gouv.vitam.tools.sedalib.utils.SEDALibException;

import java.time.LocalDateTime;
import java.util.Date;

class ContentTest {

    @Test
    // Test Content and ComplexListType subclass
    void testConstructors() throws SEDALibException {
        // Given
        Content c = new Content();

        // When loaded with all different kind of metadata

        //Test StringType metadata
        StringType stringType = new StringType("DescriptionLevel", "Series");
        c.addMetadata(stringType);

        // Test TextType metadata
        TextType textType = new TextType("Description", "TestDescription EN", "en");
        c.addMetadata(textType);

        // Test AgencyType metadata
        AgencyType agencyType = new AgencyType("OriginatingAgency", "Agency1");
        c.addMetadata(agencyType);

        // Test AgentType and PlaceType metadata
        AgentType writer = new AgentType("Writer");
        writer.addNewMetadata("FirstName", "TestPrenom");
        writer.addNewMetadata("BirthName", "TestNom");
        writer.addNewMetadata("Identifier", "ID1");
        writer.addNewMetadata("Identifier", "ID2");
        writer.addNewMetadata("Function", "F1");
        PlaceType birthPlace=new PlaceType("BirthPlace");
        birthPlace.addNewMetadata("Geogname","Location1");
        birthPlace.addNewMetadata("Country","Country1");
        writer.addMetadata(birthPlace);
        c.addMetadata(writer);

        // Test AnyXMLType metadata
        AnyXMLType anyXMLType=new AnyXMLType("XMLTest", "<XMLTest><TestTag>TestValue</TestTag></XMLTest>");
        c.addMetadata(anyXMLType);

        // Test IntegerType metadata and expandability
        IntegerType integerType=new IntegerType("UniversalNumber",42);
        c.addMetadata(integerType);

        // Test Signe metadata
        Signer signer=new Signer("Laura Tremoulet",LocalDateTime.of(1970,1,1,2,0));
        signer.addNewMetadata("BirthPlace","TestLocation1");

        // Test Signature metadata
        Signature signature=new Signature();
        signature.addNewMetadata("Signer","Paul","Dupont",LocalDateTime.of(1970,1,1,1,0));
        signature.addNewMetadata("Signer","Martin Durant",LocalDateTime.of(1970,1,2,1,0));
        signature.addNewMetadata("Signer","Emilie","Martin",LocalDateTime.of(1970,1,2,1,0),"emilie.martin@corp.fr");
        signature.addNewMetadata("Validator" ,"The corporation",LocalDateTime.of(1970,1,3,1,0));
        signature.addNewMetadata("ReferencedObject","TestID1","TestDigest1");
        signature.addMetadata(signer);
        c.addMetadata(signature);

        // Test Coverage metadata
        Coverage coverage=new Coverage();
        coverage.addNewMetadata("Spatial","TestSpatial1");
        coverage.addNewMetadata("Spatial","TestSpatial2");
        coverage.addNewMetadata("Spatial","TestSpatial3");
        coverage.addNewMetadata("Temporal","TestTemporal1");
        coverage.addNewMetadata("Temporal","TestTemporal2");
        coverage.addNewMetadata("Juridictional","TestJuridictional1");
        c.addMetadata(coverage);

        // Test CustodialHistory metadata
        CustodialHistory custodialHistory=new CustodialHistory("TestItem1", "TestItem2", "TestItem3", "TestItem4");
        c.addMetadata(custodialHistory);

        // Test Event metadata and expandability
        Event event=new Event();
        event.addNewMetadata("EventDateTime",LocalDateTime.of(1970,1,1,1,0));
        event.addNewMetadata("Outcome","OK");
        event.addNewMetadata("Information","<Information>OK</Information>");
        c.addMetadata(event);

        // Test Keyword and KeywordType metadata
        Keyword keyword=new Keyword();
        KeywordType keywordType =new KeywordType("subject");
        keyword.addMetadata(keywordType);
        c.addMetadata(keyword);

        // Test GPS metadata
        c.addNewMetadata("Gps", "TestVersion",10000,"-TestLatitude","-TestLongitude","TestDateStamp");

        // Test AnyXMLType metadata
        c.addNewMetadata("XMLTest", "<XMLTest><TestTag>TestValue</TestTag></XMLTest>");

        // Test free content import and automatic sorting in ComplexListType
        c.addSedaXmlFragments("  <OriginatingSystemId>Cerfa-1244771-ID10000</OriginatingSystemId>\n" +
                "  <RegisteredDate>2104-05-13T00:00:00</RegisteredDate>\n" +
                "  <Event>\n" +
                "    <EventIdentifier>AUT-234452</EventIdentifier>\n" +
                "    <EventTypeCode>Autorisation</EventTypeCode>\n" +
                "    <EventDateTime>2104-05-31T01:00:00</EventDateTime>\n" +
                "    <Outcome>OK</Outcome>\n" +
                "  </Event>\n" +
                "  <Frog>\n" +
                "    <CommonName>Rouge à laser</CommonName>\n" +
                "    <ScientificName>Rubra Rana Laseri</ScientificName>\n" +
                "  </Frog>\n" +
                "  <Requirer>\n" +
                "    <Identifier>REF-16F14A19BF22</Identifier>\n" +
                "    <FirstName>Edouard</FirstName>\n" +
                "    <BirthName>AFINA</BirthName>\n" +
                "  </Requirer>\n" +
                "  <Frog>\n" +
                "    <CommonName>Verte à détente</CommonName>\n" +
                "    <ScientificName>Viridi Rana Elasticis</ScientificName>\n" +
                "  </Frog>");

        String cOut = c.toString();

        // Test read write in XML string format
        Content cNext = (Content) SEDAMetadata.fromString(cOut, Content.class);
        String cNextOut = cNext.toString();

        // Then

        String testOut = "<Content>\n" +
                "  <DescriptionLevel>Series</DescriptionLevel>\n" +
                "  <OriginatingSystemId>Cerfa-1244771-ID10000</OriginatingSystemId>\n" +
                "  <Description xml:lang=\"en\">TestDescription EN</Description>\n" +
                "  <CustodialHistory>\n" +
                "    <CustodialHistoryItem>TestItem1</CustodialHistoryItem>\n" +
                "    <CustodialHistoryItem>TestItem2</CustodialHistoryItem>\n" +
                "    <CustodialHistoryItem>TestItem3</CustodialHistoryItem>\n" +
                "    <CustodialHistoryItem>TestItem4</CustodialHistoryItem>\n" +
                "  </CustodialHistory>\n" +
                "  <Keyword>\n" +
                "    <KeywordType>subject</KeywordType>\n" +
                "  </Keyword>\n" +
                "  <Coverage>\n" +
                "    <Spatial>TestSpatial1</Spatial>\n" +
                "    <Spatial>TestSpatial2</Spatial>\n" +
                "    <Spatial>TestSpatial3</Spatial>\n" +
                "    <Temporal>TestTemporal1</Temporal>\n" +
                "    <Temporal>TestTemporal2</Temporal>\n" +
                "    <Juridictional>TestJuridictional1</Juridictional>\n" +
                "  </Coverage>\n" +
                "  <OriginatingAgency>\n" +
                "    <Identifier>Agency1</Identifier>\n" +
                "  </OriginatingAgency>\n" +
                "  <Writer>\n" +
                "    <FirstName>TestPrenom</FirstName>\n" +
                "    <BirthName>TestNom</BirthName>\n" +
                "    <BirthPlace>\n" +
                "      <Geogname>Location1</Geogname>\n" +
                "      <Country>Country1</Country>\n" +
                "    </BirthPlace>\n" +
                "    <Identifier>ID1</Identifier>\n" +
                "    <Identifier>ID2</Identifier>\n" +
                "    <Function>F1</Function>\n" +
                "  </Writer>\n" +
                "  <RegisteredDate>2104-05-13T00:00:00</RegisteredDate>\n" +
                "  <Event>\n" +
                "    <EventDateTime>1970-01-01T01:00:00</EventDateTime>\n" +
                "    <Outcome>OK</Outcome>\n" +
                "    <Information>OK</Information>\n" +
                "  </Event>\n" +
                "  <Event>\n" +
                "    <EventIdentifier>AUT-234452</EventIdentifier>\n" +
                "    <EventTypeCode>Autorisation</EventTypeCode>\n" +
                "    <EventDateTime>2104-05-31T01:00:00</EventDateTime>\n" +
                "    <Outcome>OK</Outcome>\n" +
                "  </Event>\n" +
                "  <Signature>\n" +
                "    <Signer>\n" +
                "      <FirstName>Paul</FirstName>\n" +
                "      <BirthName>Dupont</BirthName>\n" +
                "      <SigningTime>1970-01-01T01:00:00</SigningTime>\n" +
                "    </Signer>\n" +
                "    <Signer>\n" +
                "      <FullName>Martin Durant</FullName>\n" +
                "      <SigningTime>1970-01-02T01:00:00</SigningTime>\n" +
                "    </Signer>\n" +
                "    <Signer>\n" +
                "      <FirstName>Emilie</FirstName>\n" +
                "      <BirthName>Martin</BirthName>\n" +
                "      <Identifier>emilie.martin@corp.fr</Identifier>\n" +
                "      <SigningTime>1970-01-02T01:00:00</SigningTime>\n" +
                "    </Signer>\n" +
                "    <Signer>\n" +
                "      <FullName>Laura Tremoulet</FullName>\n" +
                "      <BirthPlace>\n" +
                "        <Geogname>TestLocation1</Geogname>\n" +
                "      </BirthPlace>\n" +
                "      <SigningTime>1970-01-01T02:00:00</SigningTime>\n" +
                "    </Signer>\n" +
                "    <Validator>\n" +
                "      <Corpname>The corporation</Corpname>\n" +
                "      <ValidationTime>1970-01-03T01:00:00</ValidationTime>\n" +
                "    </Validator>\n" +
                "    <ReferencedObject>\n" +
                "      <SignedObjectID>TestID1</SignedObjectID>\n" +
                "      <SignedObjectDigest>TestDigest1</SignedObjectDigest>\n" +
                "    </ReferencedObject>\n" +
                "  </Signature>\n" +
                "  <Gps>\n" +
                "    <GpsVersionID>TestVersion</GpsVersionID>\n" +
                "    <GpsAltitude>10000</GpsAltitude>\n" +
                "    <GpsAltitudeRef>0</GpsAltitudeRef>\n" +
                "    <GpsLatitude>TestLatitude</GpsLatitude>\n" +
                "    <GpsLatitudeRef>S</GpsLatitudeRef>\n" +
                "    <GpsLongitude>TestLongitude</GpsLongitude>\n" +
                "    <GpsLongitudeRef>O</GpsLongitudeRef>\n" +
                "    <GpsDateStamp>TestDateStamp</GpsDateStamp>\n" +
                "  </Gps>\n" +
                "  <XMLTest>\n" +
                "    <TestTag>TestValue</TestTag>\n" +
                "  </XMLTest>\n" +
                "  <XMLTest>\n" +
                "    <TestTag>TestValue</TestTag>\n" +
                "  </XMLTest>\n" +
                "  <UniversalNumber>42</UniversalNumber>\n" +
                "  <Frog>\n" +
                "    <CommonName>Rouge à laser</CommonName>\n" +
                "    <ScientificName>Rubra Rana Laseri</ScientificName>\n" +
                "  </Frog>\n" +
                "  <Frog>\n" +
                "    <CommonName>Verte à détente</CommonName>\n" +
                "    <ScientificName>Viridi Rana Elasticis</ScientificName>\n" +
                "  </Frog>\n" +
                "  <Requirer>\n" +
                "    <Identifier>REF-16F14A19BF22</Identifier>\n" +
                "    <FirstName>Edouard</FirstName>\n" +
                "    <BirthName>AFINA</BirthName>\n" +
                "  </Requirer>\n" +
                "</Content>";
        assertThat(cNextOut).isEqualTo(testOut);

    }

    @Test
    void testSpecificMetadata() throws SEDALibException {
        // Given
        Content c = new Content();

        // When loaded with all metadata with specific kind of creation
        c.addNewMetadata("Gps", null, -100000, "TestLatitude", "TestLongitude", null);
        c.addNewMetadata("Keyword", "TestKeywordContent", null, null);

        String cOut = c.toString();

        // Then
        String testOut = "<Content>\n" +
                "  <Keyword>\n" +
                "    <KeywordContent>TestKeywordContent</KeywordContent>\n" +
                "  </Keyword>\n" +
                "  <Gps>\n" +
                "    <GpsLatitude>TestLatitude</GpsLatitude>\n" +
                "    <GpsLatitudeRef>N</GpsLatitudeRef>\n" +
                "    <GpsLongitude>TestLongitude</GpsLongitude>\n" +
                "    <GpsLongitudeRef>E</GpsLongitudeRef>\n" +
                "  </Gps>\n" +
                "</Content>";
        assertThat(cOut).isEqualTo(testOut);
    }


    @Test
    void testSomeExceptions() {
        // Given
        Content c = new Content();

        // When loaded with all different kind of metadata

        //Test wrong args in addNewMetadata
        assertThatThrownBy(() -> c.addNewMetadata("DescriptionLevel", "Test1","Test2"))
                .hasMessageContaining("Pas de constructeur"); // for StringType
        assertThatThrownBy(() -> c.addNewMetadata("Description", "Test1","Test2","Test3"))
                .hasMessageContaining("Pas de constructeur"); // for TextType
        assertThatThrownBy(() -> c.addNewMetadata("Recipient", "Test", 1))
                .hasMessageContaining("Pas de constructeur"); // for AgentType
        assertThatThrownBy(() -> c.addNewMetadata("XMLTest", new Date(0)))
                .hasMessageContaining("Pas de constructeur"); // for GenericXMLBlock
        Event event=new Event();
        assertThatThrownBy(() -> event.addNewMetadata("EventDateTime", "Date"))
                .hasMessageContaining("Pas de constructeur"); // for DateTimeType
        // Test Keyword metadata with wrong KeywordType
        assertThatThrownBy(()->c.addNewMetadata("Keyword", "TestKeywordContent", "TestKeywordReference", "notconvenient"))
                .hasMessageContaining("Valeur interdite");

    }

    @Test
    void testAddNewMetadata() throws SEDALibException {
        // Given
        Content c = new Content();

        // When loaded with all different kind of metadata (in random order)

        // Test expandability
        AgentType beneficiary=new AgentType("Beneficiary","TestFirstName","TestBirthName","TestIdentifier");
        c.addMetadata(beneficiary);

        // Test Signer metadata
        Signature signature=new Signature();
        signature.addNewMetadata("Signer","TestFullName",LocalDateTime.of(1970,1,1,1,0));
        signature.addNewMetadata("Signer","TestFirstName","TestBirthName",LocalDateTime.of(1970,1,1,1,0));
        signature.addNewMetadata("Signer","TestFirstName","TestBirthName",LocalDateTime.of(1970,1,1,1,0),"TestIdentifier");
        signature.addNewMetadata("Validator","TestFullName",LocalDateTime.of(1970,1,1,1,0));
        signature.addNewMetadata("ReferencedObject","TestSystemID","TestDigest");
        c.addMetadata(signature);

        // Test Keyword metadata
        c.addNewMetadata("Keyword","TestKeywordContent","TestKeywordReference","corpname");

        // Test Keyword metadata
        c.addNewMetadata("Keyword","TestKeywordContent2",null,null);

        // Test GPS metadata
        c.addNewMetadata("Gps", "TestVersion",10000,"-TestLatitude","-TestLongitude","TestDateStamp");

        // Test Event metadata
        c.addNewMetadata("Event","TestEventIdentifier2", null, null, null);
        c.addNewMetadata("Event","TestEventIdentifier", "TestEventType",
                LocalDateTime.of(1970,1,1,1,0), "TestOutcome");

        // Test CustodialHistory metadata
        c.addNewMetadata("CustodialHistory", "TestItem1", "TestItem2", "TestItem3", "TestItem4");

        // Test Coverage metadata
        Coverage coverage=new Coverage();
        coverage.addNewMetadata("Spatial","TestSpatial1");
        coverage.addNewMetadata("Spatial","TestSpatial2");
        coverage.addNewMetadata("Spatial","TestSpatial3");
        coverage.addNewMetadata("Temporal","TestTemporal1");
        coverage.addNewMetadata("Temporal","TestTemporal2");
        coverage.addNewMetadata("Juridictional","TestJuridictional1");
        c.addMetadata(coverage);

        // Test AgencyType metadata
        c.addNewMetadata("OriginatingAgency","TestAgencyIdentifier");

        // Test AgentType metadata
        c.addNewMetadata("Writer","TestFullName");
        c.addNewMetadata("Writer","TestFirstName","TestBirthName");
        c.addNewMetadata("Writer","TestFirstName","TestBirthName","TestIdentifier");
        AgentType writer=new AgentType("Writer","TestFullName");
        writer.addNewMetadata("BirthPlace","TestGeogname");
        c.addMetadata(writer);

        // Test DateTimeType metadata
        c.addNewMetadata("CreatedDate",LocalDateTime.of(2019,1,1,1,0));

        // Test TextType metadata
        c.addNewMetadata("Description", "TestDescription");
        c.addNewMetadata("Title", "TestTitle");
        c.addNewMetadata("Title", "TestTitleLang", "de"); // verify that many values are accumulated

        //Test StringType metadata
        c.addNewMetadata("DescriptionLevel", "File");
        c.addNewMetadata("DescriptionLevel", "RecordGrp"); // verify that uniq metadata is overwritten

        // Test AnyXML and expandability
        c.addSedaXmlFragments("  <OriginatingSystemId>Cerfa-1244771-ID10000</OriginatingSystemId>\n" +
                "  <RegisteredDate>2104-05-13T00:00:00</RegisteredDate>\n" +
                "  <Event>\n" +
                "    <EventIdentifier>AUT-234452</EventIdentifier>\n" +
                "    <EventTypeCode>Autorisation</EventTypeCode>\n" +
                "    <EventDateTime>2104-05-31T01:00:00</EventDateTime>\n" +
                "    <Outcome>OK</Outcome>\n" +
                "  </Event>\n" +
                "  <Frog>\n" +
                "    <CommonName>Rouge à laser</CommonName>\n" +
                "    <ScientificName>Rubra Rana Laseri</ScientificName>\n" +
                "  </Frog>\n" +
                "  <Requirer>\n" +
                "    <Identifier>REF-16F14A19BF22</Identifier>\n" +
                "    <FirstName>Edouard</FirstName>\n" +
                "    <BirthName>AFINA</BirthName>\n" +
                "  </Requirer>\n" +
                "  <Frog>\n" +
                "    <CommonName>Verte à détente</CommonName>\n" +
                "    <ScientificName>Viridi Rana Elasticis</ScientificName>\n" +
                "  </Frog>");

        String cOut = c.toString();

        // Test read write in XML string format
        Content cNext = (Content) SEDAMetadata.fromString(cOut, Content.class);
        String cNextOut = cNext.toString();

        // Then

        String testOut = "<Content>\n" +
                "  <DescriptionLevel>RecordGrp</DescriptionLevel>\n" +
                "  <Title>TestTitle</Title>\n" +
                "  <Title xml:lang=\"de\">TestTitleLang</Title>\n" +
                "  <OriginatingSystemId>Cerfa-1244771-ID10000</OriginatingSystemId>\n" +
                "  <Description>TestDescription</Description>\n" +
                "  <CustodialHistory>\n" +
                "    <CustodialHistoryItem>TestItem1</CustodialHistoryItem>\n" +
                "    <CustodialHistoryItem>TestItem2</CustodialHistoryItem>\n" +
                "    <CustodialHistoryItem>TestItem3</CustodialHistoryItem>\n" +
                "    <CustodialHistoryItem>TestItem4</CustodialHistoryItem>\n" +
                "  </CustodialHistory>\n" +
                "  <Keyword>\n" +
                "    <KeywordContent>TestKeywordContent</KeywordContent>\n" +
                "    <KeywordReference>TestKeywordReference</KeywordReference>\n" +
                "    <KeywordType>corpname</KeywordType>\n" +
                "  </Keyword>\n" +
                "  <Keyword>\n" +
                "    <KeywordContent>TestKeywordContent2</KeywordContent>\n" +
                "  </Keyword>\n" +
                "  <Coverage>\n" +
                "    <Spatial>TestSpatial1</Spatial>\n" +
                "    <Spatial>TestSpatial2</Spatial>\n" +
                "    <Spatial>TestSpatial3</Spatial>\n" +
                "    <Temporal>TestTemporal1</Temporal>\n" +
                "    <Temporal>TestTemporal2</Temporal>\n" +
                "    <Juridictional>TestJuridictional1</Juridictional>\n" +
                "  </Coverage>\n" +
                "  <OriginatingAgency>\n" +
                "    <Identifier>TestAgencyIdentifier</Identifier>\n" +
                "  </OriginatingAgency>\n" +
                "  <Writer>\n" +
                "    <FullName>TestFullName</FullName>\n" +
                "  </Writer>\n" +
                "  <Writer>\n" +
                "    <FirstName>TestFirstName</FirstName>\n" +
                "    <BirthName>TestBirthName</BirthName>\n" +
                "  </Writer>\n" +
                "  <Writer>\n" +
                "    <FirstName>TestFirstName</FirstName>\n" +
                "    <BirthName>TestBirthName</BirthName>\n" +
                "    <Identifier>TestIdentifier</Identifier>\n" +
                "  </Writer>\n" +
                "  <Writer>\n" +
                "    <FullName>TestFullName</FullName>\n" +
                "    <BirthPlace>\n" +
                "      <Geogname>TestGeogname</Geogname>\n" +
                "    </BirthPlace>\n" +
                "  </Writer>\n" +
                "  <CreatedDate>2019-01-01T01:00:00</CreatedDate>\n" +
                "  <RegisteredDate>2104-05-13T00:00:00</RegisteredDate>\n" +
                "  <Event>\n" +
                "    <EventIdentifier>TestEventIdentifier2</EventIdentifier>\n" +
                "  </Event>\n" +
                "  <Event>\n" +
                "    <EventIdentifier>TestEventIdentifier</EventIdentifier>\n" +
                "    <EventType>TestEventType</EventType>\n" +
                "    <EventDateTime>1970-01-01T01:00:00</EventDateTime>\n" +
                "    <Outcome>TestOutcome</Outcome>\n" +
                "  </Event>\n" +
                "  <Event>\n" +
                "    <EventIdentifier>AUT-234452</EventIdentifier>\n" +
                "    <EventTypeCode>Autorisation</EventTypeCode>\n" +
                "    <EventDateTime>2104-05-31T01:00:00</EventDateTime>\n" +
                "    <Outcome>OK</Outcome>\n" +
                "  </Event>\n" +
                "  <Signature>\n" +
                "    <Signer>\n" +
                "      <FullName>TestFullName</FullName>\n" +
                "      <SigningTime>1970-01-01T01:00:00</SigningTime>\n" +
                "    </Signer>\n" +
                "    <Signer>\n" +
                "      <FirstName>TestFirstName</FirstName>\n" +
                "      <BirthName>TestBirthName</BirthName>\n" +
                "      <SigningTime>1970-01-01T01:00:00</SigningTime>\n" +
                "    </Signer>\n" +
                "    <Signer>\n" +
                "      <FirstName>TestFirstName</FirstName>\n" +
                "      <BirthName>TestBirthName</BirthName>\n" +
                "      <Identifier>TestIdentifier</Identifier>\n" +
                "      <SigningTime>1970-01-01T01:00:00</SigningTime>\n" +
                "    </Signer>\n" +
                "    <Validator>\n" +
                "      <Corpname>TestFullName</Corpname>\n" +
                "      <ValidationTime>1970-01-01T01:00:00</ValidationTime>\n" +
                "    </Validator>\n" +
                "    <ReferencedObject>\n" +
                "      <SignedObjectID>TestSystemID</SignedObjectID>\n" +
                "      <SignedObjectDigest>TestDigest</SignedObjectDigest>\n" +
                "    </ReferencedObject>\n" +
                "  </Signature>\n" +
                "  <Gps>\n" +
                "    <GpsVersionID>TestVersion</GpsVersionID>\n" +
                "    <GpsAltitude>10000</GpsAltitude>\n" +
                "    <GpsAltitudeRef>0</GpsAltitudeRef>\n" +
                "    <GpsLatitude>TestLatitude</GpsLatitude>\n" +
                "    <GpsLatitudeRef>S</GpsLatitudeRef>\n" +
                "    <GpsLongitude>TestLongitude</GpsLongitude>\n" +
                "    <GpsLongitudeRef>O</GpsLongitudeRef>\n" +
                "    <GpsDateStamp>TestDateStamp</GpsDateStamp>\n" +
                "  </Gps>\n" +
                "  <Beneficiary>\n" +
                "    <FirstName>TestFirstName</FirstName>\n" +
                "    <BirthName>TestBirthName</BirthName>\n" +
                "    <Identifier>TestIdentifier</Identifier>\n" +
                "  </Beneficiary>\n" +
                "  <Frog>\n" +
                "    <CommonName>Rouge à laser</CommonName>\n" +
                "    <ScientificName>Rubra Rana Laseri</ScientificName>\n" +
                "  </Frog>\n" +
                "  <Frog>\n" +
                "    <CommonName>Verte à détente</CommonName>\n" +
                "    <ScientificName>Viridi Rana Elasticis</ScientificName>\n" +
                "  </Frog>\n" +
                "  <Requirer>\n" +
                "    <Identifier>REF-16F14A19BF22</Identifier>\n" +
                "    <FirstName>Edouard</FirstName>\n" +
                "    <BirthName>AFINA</BirthName>\n" +
                "  </Requirer>\n" +
                "</Content>";
        assertThat(cNextOut).isEqualTo(testOut);
    }
}
