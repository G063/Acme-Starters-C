<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="spokesperson.campaign.list.label.ticker" path="ticker"/>
	<acme:list-column code="spokesperson.campaign.list.label.name" path="name"/>
	<acme:list-column code="spokesperson.campaign.list.label.effort" path="effort"/>
	<acme:list-column code="spokesperson.campaign.list.label.draftMode" path="draftMode"/>
</acme:list>

<acme:button code="spokesperson.campaign.list.button.create" action="/spokesperson/campaign/create"/>
