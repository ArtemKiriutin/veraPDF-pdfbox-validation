/**
 * This file is part of veraPDF PDF Box PDF/A Validation Model Implementation, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF PDF Box PDF/A Validation Model Implementation is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF PDF Box PDF/A Validation Model Implementation as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.model.impl.pb.pd.colors;

import org.apache.pdfbox.pdmodel.graphics.color.PDICCBased;
import org.verapdf.model.pdlayer.PDICCBasedCMYK;

/**
 * @author Maksim Bezrukov
 */
public class PBoxPDICCBasedCMYK extends PBoxPDICCBased implements PDICCBasedCMYK {

	public static final String ICC_BASED_CMYK_TYPE = "PDICCBasedCMYK";

	private final Long opm;
	private final Boolean overprintingFlag;

	public PBoxPDICCBasedCMYK(PDICCBased simplePDObject, int op, boolean overprintingFlag) {
		super(simplePDObject, ICC_BASED_CMYK_TYPE);
		this.opm = Long.valueOf(op);
		this.overprintingFlag = Boolean.valueOf(overprintingFlag);
	}

	@Override
	public Long getOPM() {
		return this.opm;
	}

	@Override
	public Boolean getoverprintFlag() {
		return this.overprintingFlag;
	}
}
