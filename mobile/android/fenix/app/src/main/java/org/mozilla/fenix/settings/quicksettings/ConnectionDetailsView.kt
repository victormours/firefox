/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.settings.quicksettings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import mozilla.components.browser.icons.BrowserIcons
import org.mozilla.fenix.R
import org.mozilla.fenix.databinding.ConnectionDetailsWebsiteInfoBinding
import org.mozilla.fenix.ext.loadIntoView

/**
 * MVI View that knows to display a whether the current website connection details.
 *
 * Currently it does not support any user interaction.
 *
 * @param container [ViewGroup] in which this View will inflate itself.
 * @param icons Icons component for loading, caching and processing website icons.
 * @param interactor [WebSiteInfoInteractor] which will have delegated to all user interactions.
 */
class ConnectionDetailsView(
    container: ViewGroup,
    private val icons: BrowserIcons,
    private val interactor: WebSiteInfoInteractor,
) {
    val binding = ConnectionDetailsWebsiteInfoBinding.inflate(
        LayoutInflater.from(container.context),
        container,
        true,
    )

    /**
     * Allows changing what this View displays.
     *
     * @param state [WebsiteInfoState] to be rendered.
     */
    fun update(state: WebsiteInfoState) {
        icons.loadIntoView(binding.faviconImage, state.websiteUrl)
        bindUrl(state.websiteUrl)
        bindGovernmentInfo(state)
        bindSecurityInfo(state.websiteInfoUiValues)
        bindCertificateName(state.certificateName)
        bindTitle(state.websiteTitle)
        bindBackButtonListener()
    }

    private fun bindUrl(websiteUrl: String) {
        binding.url.text = websiteUrl
    }

    private fun bindGovernmentInfo(state: WebsiteInfoState) {
      if (state.websiteInfoUiValues == WebsiteInfoUiValues.GOVERNMENT) {
        binding.governmentInfoIcon.setImageResource(state.websiteInfoUiValues.iconRes)
        binding.governmentInfo.setText(state.websiteInfoUiValues.siteInfoRes)


        val domainLabel =
          provideContext().getString(R.string.government_info_explanation, state.governmentDomainName)
        binding.governmentDomainInfo.setText(domainLabel)


        binding.governmentInfoContainer.isVisible = true
      } else {
        binding.governmentInfoContainer.isVisible = false
      }
    }

    private fun bindSecurityInfo(uiValues: WebsiteInfoUiValues) {
      if (uiValues == WebsiteInfoUiValues.GOVERNMENT) {
        // A website has to use a secure connection to have the government value
        binding.securityInfo.setText(WebsiteInfoUiValues.SECURE.siteInfoRes)
        binding.securityInfoIcon.setImageResource(WebsiteInfoUiValues.SECURE.iconRes)
      } else {
        binding.securityInfo.setText(uiValues.siteInfoRes)
        binding.securityInfoIcon.setImageResource(uiValues.iconRes)
      }
    }

    @VisibleForTesting
    internal fun provideContext(): Context = binding.root.context

    @VisibleForTesting
    internal fun bindBackButtonListener() {
        binding.detailsBack.isVisible = true
        binding.detailsBack.setOnClickListener {
            interactor.onBackPressed()
        }
    }

    @VisibleForTesting
    internal fun bindTitle(websiteTitle: String) {
        binding.title.text = websiteTitle
        binding.titleContainer.isVisible = websiteTitle.isNotEmpty()
    }

    @VisibleForTesting
    internal fun bindCertificateName(cert: String) {
        val certificateLabel =
            provideContext().getString(R.string.certificate_info_verified_by, cert)
        binding.certificateInfo.text = certificateLabel
        binding.certificateInfo.isVisible = cert.isNotEmpty()
    }
}
