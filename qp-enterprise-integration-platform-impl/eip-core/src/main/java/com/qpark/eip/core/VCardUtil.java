/*******************************************************************************
 * Copyright (c) 2013 QPark Consulting  S.a r.l.
 * 
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Bernhard Hausen - Initial API and implementation
 *
 ******************************************************************************/
package com.qpark.eip.core;

import java.io.IOException;
import java.util.Iterator;

import net.sourceforge.cardme.engine.VCardEngine;
import net.sourceforge.cardme.io.CompatibilityMode;
import net.sourceforge.cardme.io.VCardWriter;
import net.sourceforge.cardme.vcard.VCard;
import net.sourceforge.cardme.vcard.VCardImpl;
import net.sourceforge.cardme.vcard.VCardVersion;
import net.sourceforge.cardme.vcard.features.EmailFeature;
import net.sourceforge.cardme.vcard.features.TelephoneFeature;
import net.sourceforge.cardme.vcard.types.EmailType;
import net.sourceforge.cardme.vcard.types.FormattedNameType;
import net.sourceforge.cardme.vcard.types.NameType;
import net.sourceforge.cardme.vcard.types.TelephoneType;
import net.sourceforge.cardme.vcard.types.TitleType;
import net.sourceforge.cardme.vcard.types.VersionType;
import net.sourceforge.cardme.vcard.types.parameters.EmailParameterType;
import net.sourceforge.cardme.vcard.types.parameters.TelephoneParameterType;

/**
 * Get a vCard. http://en.wikipedia.org/wiki/VCard
 * @author bhausen
 */
public class VCardUtil {
	public static void main(final String[] args) {
		try {
			VCard vcard = new VCardImpl();
			vcard.setVersion(new VersionType(VCardVersion.V3_0));

			TitleType title = new TitleType("Mr.");
			vcard.setTitle(title);

			NameType name = new NameType();
			name.setFamilyName("Duck");
			name.setGivenName("Donald");
			vcard.setName(name);

			FormattedNameType fName = new FormattedNameType("Donald Duck");
			vcard.setFormattedName(fName);

			EmailFeature email = new EmailType();
			email.setEmail("donald.duck@ses.com");
			email.addEmailParameterType(EmailParameterType.INTERNET);
			email.addEmailParameterType(EmailParameterType.PREF);
			vcard.addEmail(email);
			email = new EmailType();
			email.setEmail("donald.duck@ducktales.com");
			email.addEmailParameterType(EmailParameterType.INTERNET);
			vcard.addEmail(email);

			TelephoneFeature telephone = new TelephoneType();
			telephone.setTelephone("+352 123498 1");
			telephone.addTelephoneParameterType(TelephoneParameterType.HOME);
			telephone.addTelephoneParameterType(TelephoneParameterType.VOICE);
			vcard.addTelephoneNumber(telephone);
			telephone = new TelephoneType();
			telephone.setTelephone("+352 123498 2");
			telephone.addTelephoneParameterType(TelephoneParameterType.WORK);
			telephone.addTelephoneParameterType(TelephoneParameterType.VOICE);
			vcard.addTelephoneNumber(telephone);
			telephone = new TelephoneType();
			telephone.setTelephone("+352 123498 3");
			telephone.addTelephoneParameterType(TelephoneParameterType.FAX);
			vcard.addTelephoneNumber(telephone);
			telephone = new TelephoneType();
			telephone.setTelephone("+352 123498-4");
			telephone.addTelephoneParameterType(TelephoneParameterType.CELL);
			vcard.addTelephoneNumber(telephone);
			VCardWriter writer = new VCardWriter();
			writer.setCompatibilityMode(CompatibilityMode.RFC2426);
			writer.setVCard(vcard);
			String vstring = writer.buildVCardString();
			System.out.println(vstring);

			VCardEngine eng = new VCardEngine();
			VCard parsed = eng.parse(vstring);
			Iterator<TelephoneFeature> it = parsed.getTelephoneNumbers();
			while (it.hasNext()) {
				TelephoneFeature type = it.next();
				System.out.println(type.getTelephone());
				for (Object x : type.getExtendedParametersList()) {
					System.out.println("X" + x);
				}
				System.out.println(type);

			}
			System.out.println("##################################");
			System.out.println("##################################");
			System.out.println("##################################");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Translate a given VCard string version 4.9 into a string of VCard version
	 * 3.0.
	 * @param vcardVersion49 The string of version 4.9.
	 * @return The string of version 3.0. If parsing fails the
	 *         <code>vcardVersion49</code> will be returned.
	 */
	public static String translateToVCardVersion30(final String vcardVersion49) {
		String vcardString = null;
		if (vcardVersion49 != null) {
			try {
				VCardEngine vcardEngine = new VCardEngine();
				VCard vcard = vcardEngine.parse(vcardVersion49);
				VCardWriter writer = new VCardWriter();
				writer.setVCard(vcard);
				vcardString = writer.buildVCardString();
			} catch (IOException e) {
				vcardString = vcardVersion49;
			}
		}
		return vcardString;
	}

	/**
	 * Translate a given VCard string version 4.9 into a string of VCard version
	 * 3.0.
	 * @param vcardVersion49 The string of version 4.9.
	 * @return The string of version 4.0. If parsing fails the
	 *         <code>vcardVersion49</code> will be returned.
	 */
	public static String translateToVCardVersion40(final String vcardVersion49) {
		String vcardString = null;
		if (vcardVersion49 != null) {
			try {
				VCardEngine vcardEngine = new VCardEngine();
				VCard vcard = vcardEngine.parse(vcardVersion49);
				VCardWriter writer = new VCardWriter();
				writer.setVCard(vcard);
				writer.setOutputVersion(VCardVersion.V4_0);
				vcardString = writer.buildVCardString();
			} catch (IOException e) {
				vcardString = vcardVersion49;
			}
		}
		return vcardString;
	}

	public static boolean emailContains(final VCard vcard, final String email) {
		boolean contains = false;
		if (vcard != null && email != null) {
			for (Iterator<EmailFeature> iter = vcard.getEmails(); iter
					.hasNext();) {
				if (iter.next().getEmail().contains(email.trim())) {
					contains = true;
					break;
				}
			}
		}
		return contains;
	}

	public static boolean firstnameContains(final VCard vcard,
			final String firstname) {
		boolean contains = false;
		if (vcard != null && firstname != null) {
			if (vcard.getName() != null
					&& vcard.getName().getGivenName() != null) {
				contains = vcard.getName().getGivenName()
						.contains(firstname.trim());
			} else if (vcard.getFormattedName() != null) {
				contains = vcard.getFormattedName().getFormattedName()
						.contains(firstname.trim());
			}
		}
		return contains;
	}

	public static boolean lastnameContains(final VCard vcard,
			final String lastname) {
		boolean contains = false;
		if (vcard != null && lastname != null) {
			if (vcard.getName() != null
					&& vcard.getName().getFamilyName() != null) {
				contains = vcard.getName().getFamilyName()
						.contains(lastname.trim());
			} else if (vcard.getFormattedName() != null) {
				contains = vcard.getFormattedName().getFormattedName()
						.contains(lastname.trim());
			}
		}
		return contains;
	}

	/**
	 * @param firstname
	 * @param lastname non empty string.
	 * @param email
	 * @return the vCard.
	 */
	public static VCard toVCard(final String firstname, final String lastname,
			final String email, final String phone) {
		VCard vcard = new VCardImpl();
		vcard.setVersion(new VersionType(VCardVersion.V3_0));

		vcard.setFormattedName(new FormattedNameType(
				new StringBuffer(firstname).append(" ").append(lastname)
						.toString()));
		NameType name = new NameType();
		name.setFamilyName(lastname);
		name.setGivenName(firstname);
		vcard.setName(name);

		EmailFeature em = new EmailType();
		em.setEmail(email);
		em.addEmailParameterType(EmailParameterType.WORK);
		vcard.addEmail(em);

		TelephoneFeature telephone = new TelephoneType();
		telephone.setTelephone(phone);
		telephone.addTelephoneParameterType(TelephoneParameterType.WORK);
		vcard.addTelephoneNumber(telephone);
		return vcard;
	}

	/**
	 * @param vcardString
	 * @return the vCard.
	 */
	public static VCard toVCard(final String vcardString) {
		VCard vcard = null;
		if (vcardString != null) {
			try {
				VCardEngine vcardEngine = new VCardEngine();
				vcard = vcardEngine.parse(vcardString);
				vcard.setVersion(new VersionType(VCardVersion.V3_0));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return vcard;
	}

	public static String toString(final VCard vcard) {
		VCardWriter writer = new VCardWriter();
		writer.setCompatibilityMode(CompatibilityMode.RFC2426);
		writer.setVCard(vcard);
		String vcardString = writer.buildVCardString();
		return vcardString;
	}
}
