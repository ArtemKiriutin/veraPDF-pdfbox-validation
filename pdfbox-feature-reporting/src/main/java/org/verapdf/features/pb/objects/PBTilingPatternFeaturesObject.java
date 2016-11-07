package org.verapdf.features.pb.objects;

import java.util.Set;

import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;

/**
 * Feature object for tilling pattern
 *
 * @author Maksim Bezrukov
 */
public class PBTilingPatternFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private PDTilingPattern tilingPattern;
	private String id;
	private Set<String> extGStateChild;
	private Set<String> colorSpaceChild;
	private Set<String> patternChild;
	private Set<String> shadingChild;
	private Set<String> xobjectChild;
	private Set<String> fontChild;
	private Set<String> propertiesChild;

	/**
	 * Constructs new tilling pattern features object
	 *
	 * @param tilingPattern   PDTilingPattern which represents tilling pattern for feature report
	 * @param id              id of the object
	 * @param extGStateChild  set of external graphics state id which contains in resource dictionary of this pattern
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this pattern
	 * @param patternChild    set of pattern id which contains in resource dictionary of this pattern
	 * @param shadingChild    set of shading id which contains in resource dictionary of this pattern
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this pattern
	 * @param fontChild       set of font id which contains in resource dictionary of this pattern
	 * @param propertiesChild set of properties id which contains in resource dictionary of this pattern
	 */
	public PBTilingPatternFeaturesObject(PDTilingPattern tilingPattern, String id, Set<String> extGStateChild,
										 Set<String> colorSpaceChild, Set<String> patternChild, Set<String> shadingChild,
										 Set<String> xobjectChild, Set<String> fontChild, Set<String> propertiesChild) {
		this.tilingPattern = tilingPattern;
		this.id = id;
		this.extGStateChild = extGStateChild;
		this.colorSpaceChild = colorSpaceChild;
		this.patternChild = patternChild;
		this.shadingChild = shadingChild;
		this.xobjectChild = xobjectChild;
		this.fontChild = fontChild;
		this.propertiesChild = propertiesChild;
	}

	/**
	 * @return PATTERN instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.PATTERN;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {
		if (tilingPattern != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("pattern");
			if (id != null) {
				root.setAttribute(ID, id);
			}
			root.setAttribute("type", "tiling");

			root.addChild("paintType").setValue(String.valueOf(tilingPattern.getPaintType()));
			root.addChild("tilingType").setValue(String.valueOf(tilingPattern.getTilingType()));

			PBCreateNodeHelper.addBoxFeature("bbox", tilingPattern.getBBox(), root);

			root.addChild("xStep").setValue(String.valueOf(tilingPattern.getXStep()));
			root.addChild("yStep").setValue(String.valueOf(tilingPattern.getYStep()));

			parseFloatMatrix(tilingPattern.getMatrix().getValues(), root.addChild("matrix"));

			parseResources(root);

			collection.addNewFeatureTree(FeatureObjectType.PATTERN, root);
			return root;
		}

		return null;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

	private static void parseFloatMatrix(float[][] array, FeatureTreeNode parent) throws FeatureParsingException {
		for (int i = 0; i < array.length; ++i) {
			for (int j = 0; j < array.length - 1; ++j) {
				FeatureTreeNode element = parent.addChild("element");
				element.setAttribute("row", String.valueOf(i + 1));
				element.setAttribute("column", String.valueOf(j + 1));
				element.setAttribute("value", String.valueOf(array[i][j]));
			}
		}
	}

	private void parseResources(FeatureTreeNode root) throws FeatureParsingException {

		if ((extGStateChild != null && !extGStateChild.isEmpty()) ||
				(colorSpaceChild != null && !colorSpaceChild.isEmpty()) ||
				(patternChild != null && !patternChild.isEmpty()) ||
				(shadingChild != null && !shadingChild.isEmpty()) ||
				(xobjectChild != null && !xobjectChild.isEmpty()) ||
				(fontChild != null && !fontChild.isEmpty()) ||
				(propertiesChild != null && !propertiesChild.isEmpty())) {
			FeatureTreeNode resources = root.addChild("resources");

			PBCreateNodeHelper.parseIDSet(extGStateChild, "graphicsState", "graphicsStates", resources);
			PBCreateNodeHelper.parseIDSet(colorSpaceChild, "colorSpace", "colorSpaces", resources);
			PBCreateNodeHelper.parseIDSet(patternChild, "pattern", "patterns", resources);
			PBCreateNodeHelper.parseIDSet(shadingChild, "shading", "shadings", resources);
			PBCreateNodeHelper.parseIDSet(xobjectChild, "xobject", "xobjects", resources);
			PBCreateNodeHelper.parseIDSet(fontChild, "font", "fonts", resources);
			PBCreateNodeHelper.parseIDSet(propertiesChild, "propertiesDict", "propertiesDicts", resources);
		}
	}
}
