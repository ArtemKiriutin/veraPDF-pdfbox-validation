/**
 * This file is part of veraPDF Library PDF Box Features Reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library PDF Box Features Reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library PDF Box Features Reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library PDF Box Features Reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.pb.objects;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.*;
import org.verapdf.features.pb.tools.PBAdapterHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.io.IOException;
import java.util.List;

/**
 * Feature object for embedded file
 *
 * @author Maksim Bezrukov
 */
public class PBEmbeddedFileFeaturesObject implements IFeaturesObject {

	private static final Logger LOGGER = Logger
			.getLogger(PBEmbeddedFileFeaturesObject.class);

	private static final String CREATION_DATE = "creationDate";
	private static final String MOD_DATE = "modDate";

	private PDComplexFileSpecification embFile;
	private int index;

	/**
	 * Constructs new Embedded File Feature Object
	 *
	 * @param embFile pdfbox class represents Embedded File object
	 * @param index   page index
	 */
	public PBEmbeddedFileFeaturesObject(PDComplexFileSpecification embFile, int index) {
		this.embFile = embFile;
		this.index = index;
	}

	/**
	 * @return EMBEDDED_FILE instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.EMBEDDED_FILE;
	}

	/**
	 * Reports all features from the object into the collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {

		if (embFile != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("embeddedFile");
			root.setAttribute("id", "file" + index);

			PBAdapterHelper.addNotEmptyNode("fileName", embFile.getFilename(), root);
			PBAdapterHelper.addNotEmptyNode("description", embFile.getFileDescription(), root);
			COSDictionary dict = embFile.getCOSObject();
			if (dict != null) {
				PBAdapterHelper.addNotEmptyNode("afRelationship", dict.getNameAsString(COSName.getPDFName("AFRelationship")), root);
			}

			PDEmbeddedFile ef = embFile.getEmbeddedFile();
			if (ef != null) {
				PBAdapterHelper.addNotEmptyNode("subtype", ef.getSubtype(), root);

				PBAdapterHelper.addNotEmptyNode("filter", getFilters(ef.getFilters()), root);

				try {
					PBAdapterHelper.createDateNode(CREATION_DATE, root, ef.getCreationDate(), collection);
				} catch (IOException e) {
					LOGGER.debug("PDFBox error obtaining creation date", e);
					FeatureTreeNode creationDate = root.addChild(CREATION_DATE);
					ErrorsHelper.addErrorIntoCollection(collection,
							creationDate,
							e.getMessage());
				}

				try {
					PBAdapterHelper.createDateNode(MOD_DATE, root, ef.getModDate(), collection);
				} catch (IOException e) {
					LOGGER.debug("PDFBox error obtaining modification date", e);
					FeatureTreeNode modDate = root.addChild(MOD_DATE);
					ErrorsHelper.addErrorIntoCollection(collection,
							modDate,
							e.getMessage());
				}

				COSBase baseParams = ef.getStream().getDictionaryObject(COSName.PARAMS);
				if (baseParams instanceof COSDictionary) {
					COSBase baseChecksum = ((COSDictionary) baseParams).getDictionaryObject(COSName.getPDFName("CheckSum"));
					if (baseChecksum instanceof COSString) {
						COSString str = (COSString) baseChecksum;
						if (str.isHex()) {
							PBAdapterHelper.addNotEmptyNode("checkSum", str.toHexString(), root);
						} else {
							PBAdapterHelper.addNotEmptyNode("checkSum", str.getString(), root);
						}
					}
				}
				PBAdapterHelper.addNotEmptyNode("size", String.valueOf(ef.getSize()), root);
			}

			collection.addNewFeatureTree(FeatureObjectType.EMBEDDED_FILE, root);
			return root;
		}
		return null;
	}

	/**
	 * @return null if it can not get embedded file stream and features data of the embedded file in other case.
	 */
	@Override
	public FeaturesData getData() {
		try {
			PDEmbeddedFile ef = embFile.getEmbeddedFile();
			if (ef == null) {
				LOGGER.debug("Missed embedded file in PDComplexFileSpecification");
				return null;
			}

			EmbeddedFileFeaturesData.Builder builder = new EmbeddedFileFeaturesData.Builder(ef.getStream().getUnfilteredStream());

			builder.name(embFile.getFilename());
			builder.description(embFile.getFileDescription());
			COSDictionary dict = embFile.getCOSObject();
			if (dict != null) {
				builder.afRelationship(dict.getNameAsString(COSName.getPDFName("AFRelationship")));
			}
			builder.subtype(ef.getSubtype());
			builder.creationDate(ef.getCreationDate());
			builder.modDate(ef.getModDate());
			builder.size(Integer.valueOf(ef.getSize()));

			COSBase baseParams = ef.getStream().getDictionaryObject(COSName.PARAMS);
			if (baseParams instanceof COSDictionary) {
				COSBase baseChecksum = ((COSDictionary) baseParams).getDictionaryObject(COSName.getPDFName("CheckSum"));
				if (baseChecksum instanceof COSString) {
					COSString str = (COSString) baseChecksum;
					if (str.isHex()) {
						builder.checkSum(str.toHexString());
					} else {
						builder.checkSum(str.getString());
					}
				}
			}

			return builder.build();
		} catch (IOException e) {
			LOGGER.debug("Can not get embedded file stream", e);
			return null;
		}
	}

	private static String getFilters(List<COSName> list) {
		if (list != null) {
			StringBuilder builder = new StringBuilder();

			for (COSName filter : list) {
				if (filter != null && filter.getName() != null) {
					builder.append(filter.getName());
					builder.append(" ");
				}
			}

			return builder.toString().trim();
		}
		return null;
	}
}
