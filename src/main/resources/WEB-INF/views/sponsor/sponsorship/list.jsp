<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="sponsor.sponsorship.list.label.ticker" path="ticker" width="15%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.name" path="name" width="30%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.start-moment" path="startMoment" width="15%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.end-moment" path="endMoment" width="15%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.draft-mode" path="draftMode" width="10%"/>
	<acme:list-column code="sponsor.sponsorship.list.label.total-money" path="totalMoney" width="15%"/>
</acme:list>

<acme:button code="sponsor.sponsorship.list.button.create" action="/sponsor/sponsorship/create"/>
