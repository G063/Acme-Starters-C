<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.sponsorship.list.label.ticker" path="ticker" width="20%"/>
	<acme:list-column code="any.sponsorship.list.label.name" path="name" width="30%"/>
	<acme:list-column code="any.sponsorship.list.label.start-moment" path="startMoment" width="20%"/>
	<acme:list-column code="any.sponsorship.list.label.end-moment" path="endMoment" width="20%"/>
	<acme:list-column code="any.sponsorship.list.label.sponsor-im" path="sponsor.im" width="10%"/>
</acme:list>
