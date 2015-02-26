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
package com.samples.platform.core;

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

import com.qpark.eip.core.annotation.Attention;

/**
 * Get a vCard. http://en.wikipedia.org/wiki/VCard
 * @author bhausen
 */
public class VCardUtil extends com.qpark.eip.core.VCardUtil {
	public static void main(final String[] args) {
		try {
			/* See Bug 789 - vCard Example */
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
			telephone.setTelephone("+352 710725 416");
			telephone.addTelephoneParameterType(TelephoneParameterType.HOME);
			telephone.addTelephoneParameterType(TelephoneParameterType.VOICE);
			vcard.addTelephoneNumber(telephone);
			telephone = new TelephoneType();
			telephone.setTelephone("+352 710725 41");
			telephone.addTelephoneParameterType(TelephoneParameterType.WORK);
			telephone.addTelephoneParameterType(TelephoneParameterType.VOICE);
			vcard.addTelephoneNumber(telephone);
			telephone = new TelephoneType();
			telephone.setTelephone("+352 710725 41");
			telephone.addTelephoneParameterType(TelephoneParameterType.FAX);
			vcard.addTelephoneNumber(telephone);
			telephone = new TelephoneType();
			telephone.setTelephone("+352 621-170179");
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

			String s = null;
			s = "BEGIN:VCARD\nVERSION:4.0\nN:Herbert;Guckler;;;\nFN:Herbert Guckler\nTITLE:Service Engineer\nTEL;TYPE=work,voice;VALUE=uri:tel:+498938066052\nADR;TYPE=work;LABEL=\"Floriansmühlstrasse 60, Munich, 80939, Germany\":;;Floriansmühlstrasse 60;Munich;;80939;Germany\nEMAIL:Herbert.Guckler@br.de\nREV:20130422T084205Z\nEND:VCARD";
			// s =
			// "BEGIN:VCARD\nVERSION:4.0\nN:Robert;Kennedy;;;\nFN:Robert Kennedy\nTEL;TYPE=work,voice;VALUE=uri:tel:+112026267951\nTEL;TYPE=work,cell;VALUE=uri:tel:+112023093355\nTEL;TYPE=work,fax;VALUE=uri:tel:+112027830315\nADR;TYPE=work;LABEL=\"400 North Capitol Street,  Suite 650, Washington, 20001, USA\":;;400 North Capitol Street,  Suite 650;Washington;;20001;USA\nREV:20130422T085711Z\nEND:VCARD";

			System.out.println(s);
			System.out.println("##################################");
			System.out.println(translateToSapToOneSatPlan(s));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Translate a given VCard string version 4.9 into a string of VCard version
	 * 3.0.
	 * @param sapString The string of version 4.9.
	 * @return The string of version 3.0. If parsing fails the
	 *         <code>vcardVersion49</code> will be returned.
	 */
	@Attention("See bug 789")
	public static String translateToSapToOneSatPlan(final String sapString) {
		String vcardString = null;
		if (sapString != null) {
			try {
				VCardEngine vcardEngine = new VCardEngine();
				VCard sap = vcardEngine.parse(sapString);

				VCard vcard = new VCardImpl();
				vcard.setVersion(new VersionType(VCardVersion.V3_0));

				if (sap.getTitle() != null) {
					vcard.setTitle(sap.getTitle());
				}

				if (sap.getName() != null) {
					vcard.setName(sap.getName());
				}
				if (sap.getFormattedName() != null) {
					vcard.setFormattedName(sap.getFormattedName());
				} else if (sap.getName() != null) {
					vcard.setFormattedName(new FormattedNameType(
							new StringBuffer(64)
									.append(sap.getName().getGivenName())
									.append(" ")
									.append(sap.getName().getFamilyName())
									.toString()));
				}

				boolean work = false;
				boolean voice = false;
				boolean cell = false;
				boolean fax = false;
				boolean pref = false;
				Iterator<EmailFeature> emails = sap.getEmails();
				EmailFeature emailSap = null;
				EmailFeature email = null;
				EmailFeature emailFirst = null;
				EmailFeature emailSecond = null;
				while (emails.hasNext()) {
					work = false;
					pref = false;
					emailSap = emails.next();
					email = new EmailType(emailSap.getEmail());
					for (EmailParameterType param : emailSap
							.getEmailParameterTypesList()) {
						if (param.equals(EmailParameterType.PREF)) {
							pref = true;
						} else if (param.equals(EmailParameterType.WORK)) {
							work = true;
						}
					}
					if (work && pref && emailFirst != null) {
						emailSecond = emailFirst;
						emailFirst = email;
					} else if (work) {
						emailFirst = email;
					} else if (emailFirst == null) {
						emailFirst = email;
					} else if (emailFirst != null && emailSecond == null) {
						emailSecond = email;
					}
				}
				Iterator<TelephoneFeature> telephones = sap
						.getTelephoneNumbers();
				TelephoneFeature telephoneSap;
				TelephoneFeature telephoneFirst = null;
				TelephoneFeature telephoneSecond = null;
				TelephoneFeature telephoneFax = null;
				while (telephones.hasNext()) {
					work = false;
					voice = false;
					cell = false;
					fax = false;
					pref = false;
					telephoneSap = telephones.next();
					TelephoneFeature telephone = new TelephoneType();
					telephone.setTelephone(telephoneSap.getTelephone());
					for (TelephoneParameterType param : telephoneSap
							.getTelephoneParameterTypesList()) {
						if (param.equals(TelephoneParameterType.CELL)) {
							cell = true;
						} else if (param.equals(TelephoneParameterType.FAX)) {
							fax = true;
						} else if (param.equals(TelephoneParameterType.WORK)) {
							work = true;
						} else if (param.equals(TelephoneParameterType.PREF)) {
							pref = true;
						} else if (param.equals(TelephoneParameterType.VOICE)) {
							voice = true;
						}
					}
					if (telephoneFirst != null && work && voice && pref) {
						telephoneSecond = telephoneFirst;
						telephoneFirst = telephone;
					} else if (work && voice) {
						telephoneFirst = telephone;
					} else if (cell && telephoneSecond == null) {
						telephoneSecond = telephone;
					} else if (fax && telephoneFax == null) {
						telephoneFax = telephone;
					}
				}

				if (telephoneFirst != null) {
					toOspCoreTelephoneFirst(telephoneFirst);
					vcard.addTelephoneNumber(telephoneFirst);
				}
				if (telephoneSecond != null) {
					toOspCoreTelephoneSecond(telephoneSecond);
					vcard.addTelephoneNumber(telephoneSecond);
				}
				if (telephoneFax != null) {
					toOspCoreTelephoneFax(telephoneFax);
					vcard.addTelephoneNumber(telephoneFax);
				}
				if (emailFirst != null) {
					toOspCoreEmailFirst(emailFirst);
					vcard.addEmail(emailFirst);
				}
				if (emailSecond != null) {
					toOspCoreEmailSecond(emailSecond);
					vcard.addEmail(emailSecond);
				}

				// if (sap.getAddresses() != null) {
				// Iterator<AddressFeature> addresses = sap.getAddresses();
				// while (addresses.hasNext()) {
				// vcard.addAddress(addresses.next());
				// }
				// }

				VCardWriter writer = new VCardWriter();
				writer.setVCard(vcard);
				vcardString = writer.buildVCardString();
			} catch (IOException e) {
				vcardString = sapString;
			}
		}
		return vcardString;
	}

	private static void toOspCoreEmailFirst(final EmailFeature t) {
		t.addEmailParameterType(EmailParameterType.INTERNET);
		t.addEmailParameterType(EmailParameterType.PREF);
	}

	private static void toOspCoreEmailSecond(final EmailFeature t) {
		t.addEmailParameterType(EmailParameterType.INTERNET);
	}

	private static void toOspCoreTelephoneFax(final TelephoneFeature t) {
		t.addTelephoneParameterType(TelephoneParameterType.FAX);
	}

	private static void toOspCoreTelephoneFirst(final TelephoneFeature t) {
		t.addTelephoneParameterType(TelephoneParameterType.HOME);
		t.addTelephoneParameterType(TelephoneParameterType.VOICE);
	}

	private static void toOspCoreTelephoneSecond(final TelephoneFeature t) {
		t.addTelephoneParameterType(TelephoneParameterType.WORK);
		t.addTelephoneParameterType(TelephoneParameterType.VOICE);
	}
}
