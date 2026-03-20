<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="spokesperson.campaign.list.label.ticker" path="ticker"/>
	<acme:list-column code="spokesperson.campaign.list.label.name" path="name"/>
	<acme:list-column code="spokesperson.campaign.list.label.effort" path="effort"/>
	<acme:list-column code="spokesperson.campaign.list.label.draftMode" path="draftMode"/>
</acme:list>

<jstl:if test="${pageContext.response.locale.language == 'es'}">
	<script type="text/javascript">
		document.addEventListener("DOMContentLoaded", function () {
			const normalizeDraftModeText = function () {
				document.querySelectorAll("table tbody td").forEach(function (cell) {
					const value = cell.textContent.trim().toLowerCase();

					if (value === "true")
						cell.textContent = "verdadero";
					else if (value === "false")
						cell.textContent = "falso";
				});
			};

			normalizeDraftModeText();

			const tableBody = document.querySelector("table tbody");
			if (tableBody) {
				const observer = new MutationObserver(function () {
					normalizeDraftModeText();
				});
				observer.observe(tableBody, {
					childList : true,
					subtree : true
				});
			}
		});
	</script>
</jstl:if>

<acme:button code="spokesperson.campaign.list.button.create" action="/spokesperson/campaign/create"/>
