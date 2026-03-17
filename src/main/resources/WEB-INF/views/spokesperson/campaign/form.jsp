<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="isCreate" value="${_command == 'create'}"/>
<jstl:set var="isDraft" value="${draftMode}"/>
<jstl:set var="isEditable" value="${isCreate || (_command == 'update') || (_command == 'show' && isDraft) || (_command == 'publish' && isDraft)}"/>
<jstl:choose>
	<jstl:when test="${isCreate}">
		<jstl:set var="titleCode" value="spokesperson.campaign.form.title.create"/>
	</jstl:when>
	<jstl:when test="${_command == 'update'}">
		<jstl:set var="titleCode" value="spokesperson.campaign.form.title.update"/>
	</jstl:when>
	<jstl:otherwise>
		<jstl:set var="titleCode" value="spokesperson.campaign.form.title.show"/>
	</jstl:otherwise>
</jstl:choose>

<h3><acme:print code="${titleCode}"/></h3>

<acme:form>
	<acme:form-textbox code="spokesperson.campaign.form.label.ticker" path="ticker" placeholder="XX00-XXXXXXXXXX" readonly="${!isEditable}"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.name" path="name" placeholder="Campaign name" readonly="${!isEditable}"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.description" path="description" placeholder="Short description" readonly="${!isEditable}"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.startMoment" path="startMoment" placeholder="yyyy/MM/dd HH:mm" readonly="${!isEditable}"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.endMoment" path="endMoment" placeholder="yyyy/MM/dd HH:mm" readonly="${!isEditable}"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.moreInfo" path="moreInfo" placeholder="https://example.com" readonly="${!isEditable}"/>

	<jstl:if test="${!isCreate}">
		<acme:form-textbox code="spokesperson.campaign.form.label.monthsActive" path="monthsActive" readonly="true"/>
		<acme:form-textbox code="spokesperson.campaign.form.label.effort" path="effort" readonly="true"/>
		<acme:form-textbox code="spokesperson.campaign.form.label.draftMode" path="draftMode" readonly="true"/>
	</jstl:if>

	<jstl:if test="${!isCreate}">
		<input type="hidden" name="id" value="${id}"/>
	</jstl:if>

	<hr/>

	<jstl:if test="${isCreate}">
		<acme:submit code="spokesperson.campaign.form.button.create" action="/spokesperson/campaign/create"/>
	</jstl:if>

	<jstl:if test="${_command == 'update' || (_command == 'show' && isDraft) || (_command == 'publish' && isDraft)}">
		<acme:submit code="spokesperson.campaign.form.button.update" action="/spokesperson/campaign/update"/>
		<acme:submit code="spokesperson.campaign.form.button.delete" action="/spokesperson/campaign/delete"/>
		<jstl:if test="${isDraft}">
			<acme:submit code="spokesperson.campaign.form.button.publish" action="/spokesperson/campaign/publish"/>
		</jstl:if>
	</jstl:if>

	<jstl:if test="${!isCreate}">
		<acme:button code="spokesperson.campaign.form.button.milestones" action="/spokesperson/milestone/list?campaignId=${id}"/>
	</jstl:if>

</acme:form>
