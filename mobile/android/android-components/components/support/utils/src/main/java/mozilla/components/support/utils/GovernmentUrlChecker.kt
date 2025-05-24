/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.support.utils

import java.net.URL
import java.net.MalformedURLException

// Copied from mobile/android/android-components/components/support/ktx/src/main/java/mozilla/components/support/ktx/kotlin/String.kt
// because I wasn't sure how to import mozilla.components.support.ktx.kotlin.tryGetHostFromUrl correctly from this directory
// But actually I think this implementation is safer, since we return an empty string in case of an error
/**
 * Tries to parse and get host part if this [String] is valid URL.
 * Otherwise returns an empty string, to avoid any false positives.
 */
private fun String.tryGetHostFromUrl(): String = try {
  URL(this).host
} catch (e: MalformedURLException) {
  ""
}


/**
 * Checks from a list of known government domains
 */
object GovernmentUrlChecker {

    /**
     * Returns the Top Level Domain for the national government matching the url, with a leading dot.
     * For instance, passing "https://example.gov.uk/something" will return ".gov.uk"
     * Returns null if there is no matching subdomain
     */
    fun findGovernmentTopLevelDomain(url: String): String? {
      // This list was compiled with the following process for each country:
      // - a Google search for "#{country} government website"
      // - The wikipedia entry for the corresponding top level domain
      // - A cross reference with [The French ministry of diplomacy website](https://www.diplomatie.gouv.fr/fr/le-ministere-et-son-reseau/representations-etrangeres-en-france/)
      // When in doubt, as the goal of this file is to clearly identify government on the web to avoid scammer posing as a government,
      // a good choice of domain is the one used by the national tax authority.
        val govTLDs = arrayOf(
          ".gov.in", // India
          ".gov.cn", // China
          ".gov", // USA
          ".go.id", // Indonesia
          ".gov.pk", // Pakistan
          ".gov.ng", // Nigeria
          ".gov.br", // Brazil
          ".gov.bd", // Bangladesh
          // I'm not sure what the proper TLD is for Russia
          ".gov.et", // Ethiopia
          ".gob.mx", // Mexico
          ".go.jp", // Japan
          ".gov.eg", // Egypt
          ".gov.ph", // Philippines
          ".gouv.cd", // Democratic Republic of the Congo
          ".gov.vn", // Vietnam
          ".gov.ir", // Iran
          ".gov.tr", // Turkey
          // I'm not sure what the proper TLD is for Germany
          ".go.th", // Thailand
          ".go.tz", // Tanzania
          ".gov.uk", // United Kingdom
          ".gouv.fr", // France
          ".gov.za", // South Africa
          ".gov.it", // Italy
          ".go.ke", // Kenya
          // Myanmar is an a political crisis, with the National Unity Government (NUG) and the State Administration Council (SAC) both claiming
          // to be the legitimate government.
          // The European Parliament is supporting the NUG (see https://www.irrawaddy.com/news/european-parliament-throws-support-behind-myanmars-shadow-government.html)
          // The NUG seems to control the .nugmyanmar.org domain (see https://trustedwebsites.nugmyanmar.org/).
          // The official government domain before the coup seemed to be .gov.mm. It seems to now be under control of the SAC (see https://www.moi.gov.mm/moi%3Aeng/news/17970)
          ".gov.co", // Columbia
          ".go.kr", // South Korea
          // There also seems to be .gov.kr, but I can't tell if it is official
          ".gov.ss", // Sudan
          ".go.ug", // Uganda
          ".gob.es", // Spain
          ".gov.dz", // Algeria
          ".gov.iq", // Iraq
          ".gob.ar", // Argentina
          ".gov.af", // Afghanistan
          ".gov.ye", // Yemen
          ".gc.ca", // Canada
          ".canada.ca", // Canada (second domain, seems like the tax authority uses this one)
          ".gov.ao", // Angola
          ".gov.ua", // Ukraine
          ".gov.ma", // Morocco
          ".gov.pl", // Poland
          ".gov.uz", // Uzbekistan
          ".gov.my", // Malaysia
          ".gov.mz", // Mozambique
          ".gov.gh", // Ghana
          ".gob.pe", // Peru
          ".gov.sa", // Saudi Arabia
          ".gov.mg", // Madagascar
          ".gouv.ci", // Côte d'Ivoire
          ".gov.cm", // Cameroon
          ".gov.np", // Nepal
          ".gob.ve", // Venezuela
          ".gouv.ne", // Niger
          ".gov.au", // Australia
          // Not sure if there is a correct domain for North Korea
          // Not sure what the correct domain is for Syria
          // Not sure what the correct domain is for Mali
          ".gov.bf", // Burkina Faso
          ".gov.lk", // Sri Lanka
          ".gov.tw", // Taiwan
          ".gov.mw", // Malawi
          ".gov.zm", // Zambia
          // Not sure what the correct domain is for Chad
          ".gov.kz", // Kazakhstan
          ".gob.cl", // Chile
          ".gov.so", // Somalia
          ".gouv.sn", // Senegal
          ".gov.ro", // Romania
          ".gob.gt", // Guatemala
          // Not sure what the correct domain is for the Netherlands
          // there is a business.gov.nl, but that seems to be the only subdomain for gov.nl, and most other public services don't use gov.nl
          ".gob.ec", // Ecuador
          ".gov.kh", // Cambodia
          ".gov.zw", // Zimbabwe
          ".gov.gn", // Guinea
          ".gouv.bj", // Benin
          ".gov.rw", // Rwanda
          ".gov.bi", // Burundi
          ".gob.bo", // Bolivia
          ".gov.tn", // Tunisia
          ".gov.ss", // South Sudan
          ".gouv.ht", // Haiti
          // Not sure what the correct domain is for Belgium
          ".gov.jo", // Jordan
          ".gob.do", // Dominican Republic
          ".gov.ae", // United Arab Emirates
          ".gob.hn", // Honduras
          ".gob.cu", // Cuba
          ".gov.tj", // Tajikistan
          ".gov.pg", // Papua New Guinea
          // Not sure what the correct domain is for Sweden
          ".gov.cz", // Czech Republic (Czechia)
          ".gov.pt", // Portugal
          ".gov.az", // Azerbaijan
          ".gov.gr", // Greece
          ".gouv.tg", // Togo
          ".gov.hu", // Hungary
          ".gov.il", // Israel
          ".gv.at", // Austria
          ".gov.by", // Belarus
          ".admin.ch", // Switzerland
          ".gov.sl", // Sierra Leone
          ".gov.la", // Laos
          ".gov.tm", // Turkmenistan
          ".gov.ly", // Libya
          ".gov.hk", // Hong Kong
          ".gov.kg", // Kyrgyzstan
          ".gov.py", // Paraguay
          // Not sure what the correct domain is for Nicaragua
          // Not sure what the correct domain is for Bulgaria
          ".gov.rs", // Serbia
          // Not sure what the correct domain is for Congo
          ".gob.sv", // El Salvador
          // Not sure what the correct domain is for Denmark
          ".gov.sg", // Singapore
          ".gov.lb", // Lebanon
          ".gov.lr", // Liberia
          // Not sure what the correct domain is for Finland
          // Not sure what the correct domain is for Norway
          ".gov.ps", // Palestine
          // Not sure what the correct domain is for the Central African Republic
          ".gov.om", // Oman
          ".gov.sk", // Slovakia
          ".gov.mr", // Mauritania
          ".gov.ie", // Ireland
          ".govt.nz", //New Zealand
          ".go.cr", // Costa Rica
          ".gov.kw", // Kuwait
          ".gob.pa", // Panama
          ".gov.hr", // Croatia
          ".gov.ge", // Georgia
          // Not sure what the correct domain is for Eritrea
          ".gov.mn", // Mongolia
          ".gub.uy", // Uruguay
          ".gov.ba", // Bosnia and Herzegovina
          ".gov.qa", // Qatar
          ".gov.na", // Namibia
          ".gov.md", // Moldova
          // Not sure what the correct domain is for Armenia
          ".gov.jm", // Jamaica
          // Not sure what the correct domain is for Lithuania
          ".gov.gm", // The Gambia
          ".gov.al", // Albania
          ".gouv.ga", // Gabon
          ".gov.bw", // Botswana
          ".gov.ls", // Lesotho
          ".gov.gw", // Guinea-Bissau
          ".gov.si", // Slovenia
          // Not sure what the correct domain is for Equatorial Guinea
          ".gov.lv", // Latvia
          ".gov.mk", // North Macedonia
          ".gov.bh", // Bahrain
          ".gov.tt", // Trinidad and Tobago
          ".gov.tl", // Timor-Leste
          ".gov.cy", //	Cyprus
          // Not sure what the correct domain is for Estonia
          // Not sure what the correct domain is for Mauritius
          ".gov.sz", // Eswatini
          ".gouv.dj", // Djibouti
          ".gov.fj", // Fiji
          ".gouv.km", // Comoros
          ".gov.sb", //	Solomon Islands
          ".gov.gy", // Guyana
          ".gov.bt", // Bhutan
          ".gov.mo", // Macao
          // Not sure what the correct domain is for Luxembourg
          ".gov.sr", // Suriname
          ".gov.me", // Montenegro
          // Not sure what the correct domain is for Western Sahara
          ".gov.mt", // Malta
          ".gov.mv", // Maldives
          ".gov.cv", // Cabo Verde
          ".gov.bn", // Brunei
          ".gov.bz", // Belize
          ".gov.bs", // Bahamas
          // Not sure what the correct domain is for Iceland
          ".gov.vu", // Vanuatu
          ".gov.bb", // Barbados
          ".gov.ws", // Samoa
        )

        val domain = url.tryGetHostFromUrl()

        return govTLDs.find { domain.endsWith(it) }
    }

}

