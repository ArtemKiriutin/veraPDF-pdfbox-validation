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

import org.verapdf.model.pdlayer.PDDeviceCMYK;

/**
 * DeviceCMYK color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceCMYK extends PBoxPDColorSpace implements PDDeviceCMYK {

    private static final PDDeviceCMYK INSTANCE = new PBoxPDDeviceCMYK(
            org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK.INSTANCE);
    private static final PDDeviceCMYK INHERITED_INSTANCE = new PBoxPDDeviceCMYK(
            org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK.INHERITED_INSTANCE);

    public static final String DEVICE_CMYK_TYPE = "PDDeviceCMYK";

    private PBoxPDDeviceCMYK(
            org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK simplePDObject) {
        super(simplePDObject, DEVICE_CMYK_TYPE);
    }

    public static PDDeviceCMYK getInstance() {
        return INSTANCE;
    }

    public static PDDeviceCMYK getInheritedInstance() {
        return INHERITED_INSTANCE;
    }
}
