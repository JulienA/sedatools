/**
 * Copyright French Prime minister Office/DINSIC/Vitam Program (2015-2019)
 *
 * contact.vitam@programmevitam.fr
 * 
 * This software is developed as a validation helper tool, for constructing Submission Information Packages (archives 
 * sets) in the Vitam program whose purpose is to implement a digital archiving back-office system managing high 
 * volumetry securely and efficiently.
 *
 * This software is governed by the CeCILL 2.1 license under French law and abiding by the rules of distribution of free
 * software. You can use, modify and/ or redistribute the software under the terms of the CeCILL 2.1 license as
 * circulated by CEA, CNRS and INRIA archiveTransfer the following URL "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and rights to copy, modify and redistribute granted by the license,
 * users are provided only with a limited warranty and the software's author, the holder of the economic rights, and the
 * successive licensors have only limited liability.
 *
 * In this respect, the user's attention is drawn to the risks associated with loading, using, modifying and/or
 * developing or reproducing the software by the user in light of its specific status of free software, that may mean
 * that it is complicated to manipulate, and that also therefore means that it is reserved for developers and
 * experienced professionals having in-depth computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling the security of their systems and/or data
 * to be ensured and, more generally, to use and operate it in the same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had knowledge of the CeCILL 2.1 license and that you
 * accept its terms.
 */
package fr.gouv.vitam.tools.sedalib.metadata;

import javax.xml.stream.XMLStreamException;

import fr.gouv.vitam.tools.sedalib.utils.SEDALibException;
import fr.gouv.vitam.tools.sedalib.xml.SEDAXMLEventReader;
import fr.gouv.vitam.tools.sedalib.xml.SEDAXMLStreamWriter;

/**
 * The Class FormatIdentification.
 * <p>
 * Class for SEDA element FormatIdentification. It's filled with
 * <a href="https://www.nationalarchives.gov.uk/PRONOM">PRONOM</a> (the TNA
 * format register) information.
 * <p>
 * A BinaryDataObject metadata.
 * <p>
 * Standard quote: "Identification du format de l'objet-données"
 */
public class FormatIdentification extends SEDAMetadata {

	// SEDA elements

	/** The format litteral. */
	public String formatLitteral;

	/** The mime type. */
	public String mimeType;

	/** The format id. */
	public String formatId;

	/** The encoding. */
	public String encoding;

	// Constructors

	/**
	 * Instantiates a new FormatIdentification.
	 */
	public FormatIdentification() {
		this(null, null, null, null);
	}

	/**
	 * Instantiates a new FormatIdentification.
	 *
	 * @param formatLitteral the format litteral
	 * @param mimeType       the mime type
	 * @param formatId       the format id
	 * @param encoding       the encoding
	 */
	public FormatIdentification(String formatLitteral, String mimeType, String formatId, String encoding) {
		this.formatLitteral = formatLitteral;
		this.mimeType = mimeType;
		this.formatId = formatId;
		this.encoding = encoding;
	}

	@Override
	public String getXmlElementName() {
		return "FormatIdentification";
	}

	@Override
	public void toSedaXml(SEDAXMLStreamWriter xmlWriter) throws SEDALibException {
		try {
			xmlWriter.writeStartElement("FormatIdentification");
			xmlWriter.writeElementValueIfNotEmpty("FormatLitteral", formatLitteral);
			xmlWriter.writeElementValueIfNotEmpty("MimeType", mimeType);
			xmlWriter.writeElementValueIfNotEmpty("FormatId", formatId);
			xmlWriter.writeElementValueIfNotEmpty("Encoding", encoding);
			xmlWriter.writeEndElement();
		} catch (XMLStreamException e) {
			throw new SEDALibException(
					"Erreur d'écriture XML dans un élément FormatIdentification\n->" + e.getMessage());
		}
	}

	/**
	 * Import the FormatIdentification in XML expected form for the SEDA Manifest.
	 *
	 * @param xmlReader the SEDAXMLEventReader reading the SEDA manifest
	 * @return the read FormatIdentification
	 * @throws SEDALibException if the XML can't be read or the SEDA scheme is not
	 *                          respected
	 */
	public static FormatIdentification fromSedaXml(SEDAXMLEventReader xmlReader) throws SEDALibException {
		FormatIdentification fi = null;
		try {
			if (xmlReader.nextBlockIfNamed("FormatIdentification")) {
				fi = new FormatIdentification();
				fi.formatLitteral = xmlReader.nextValueIfNamed("FormatLitteral");
				fi.mimeType = xmlReader.nextValueIfNamed("MimeType");
				fi.formatId = xmlReader.nextValueIfNamed("FormatId");
				fi.encoding = xmlReader.nextValueIfNamed("Encoding");
				xmlReader.endBlockNamed("FormatIdentification");
			}
		} catch (XMLStreamException | SEDALibException e) {
			throw new SEDALibException(
					"Erreur de lecture XML dans un élément FormatIdentification\n->" + e.getMessage());
		}
		return fi;
	}
}
