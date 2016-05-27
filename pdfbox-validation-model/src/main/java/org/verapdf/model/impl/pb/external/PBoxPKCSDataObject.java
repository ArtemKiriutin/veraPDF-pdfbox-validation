package org.verapdf.model.impl.pb.external;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.external.PKCSDataObject;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.ParsingException;
import sun.security.pkcs.SignerInfo;
import sun.security.x509.AlgorithmId;

import java.security.cert.X509Certificate;

/**
 * @author Sergey Shemyakov
 */
public class PBoxPKCSDataObject extends PBoxExternal implements PKCSDataObject {

	private static final Logger LOGGER = Logger.getLogger(PBoxPKCSDataObject.class);

	/** Type name for {@code PBoxPKCSDataObject} */
	public static final String PKCS_DATA_OBJECT_TYPE = "PKCSDataObject";

	protected PKCS7 pkcs7;

	/**
	 * @param pkcsData {@link COSString} containing encoded PKCS#7 object.
	 */
	public PBoxPKCSDataObject(COSString pkcsData) {
		super(PKCS_DATA_OBJECT_TYPE);
		try {
			pkcs7 = new PKCS7(pkcsData.getBytes());
		} catch (ParsingException e) {    //TODO: what do we do if some problem happens here?
			LOGGER.error("Passed PKCS7 object can't be read", e);
			pkcs7 = getEmptyPKCS7();
		}
	}

	/**
	 * @return amount of SignerInfo entries in PKCS#7 object.
	 */
	@Override
	public Long getSignerInfoCount() {
		return new Integer(pkcs7.getSignerInfos().length).longValue();
	}

	/**
	 * @return true if at least one certificate is contained in PKCS#7 object
	 * and all present certificates are not nulls.
	 */
	@Override
	public Boolean getsigningCertificatePresent() {
		X509Certificate[] certificates = pkcs7.getCertificates();
		if (certificates.length == 0) {
			return false;
		} else {
			for (X509Certificate cert : certificates) {
				if (cert == null) {
					return false;
				}
			}
		}
		return true;
	}

	private PKCS7 getEmptyPKCS7() {
		return new PKCS7(new AlgorithmId[]{}, new ContentInfo(new byte[]{}),
				new X509Certificate[]{}, new SignerInfo[]{});
	}

}
