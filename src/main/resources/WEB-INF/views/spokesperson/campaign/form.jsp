<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="isCreate" value="${_command == 'create'}"/>
<jstl:set var="isEditable" value="${isCreate || (_command == 'update')}"/>
<jstl:set var="isDraft" value="${draftMode}"/>

<acme:form>
	<acme:form-textbox code="spokesperson.campaign.form.label.ticker" path="ticker" placeholder="spokesperson.campaign.form.placeholder.ticker" readonly="${!isEditable}"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.name" path="name" placeholder="spokesperson.campaign.form.placeholder.name" readonly="${!isEditable}"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.description" path="description" placeholder="spokesperson.campaign.form.placeholder.description" readonly="${!isEditable}"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.startMoment" path="startMoment" placeholder="spokesperson.campaign.form.placeholder.date" readonly="${!isEditable}"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.endMoment" path="endMoment" placeholder="spokesperson.campaign.form.placeholder.date" readonly="${!isEditable}"/>
	<acme:form-textbox code="spokesperson.campaign.form.label.moreInfo" path="moreInfo" placeholder="spokesperson.campaign.form.placeholder.moreInfo" readonly="${!isEditable}"/>

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

	<jstl:if test="${_command == 'update'}">
		<acme:submit code="spokesperson.campaign.form.button.update" action="/spokesperson/campaign/update"/>
		<acme:submit code="spokesperson.campaign.form.button.delete" action="/spokesperson/campaign/delete"/>
		<jstl:if test="${isDraft}">
			<acme:submit code="spokesperson.campaign.form.button.publish" action="/spokesperson/campaign/publish"/>
		</jstl:if>
	</jstl:if>

	<jstl:if test="${_command == 'show' && isDraft}">
		<acme:button code="spokesperson.campaign.form.button.update" action="/spokesperson/campaign/update?id=${id}"/>
		<acme:submit code="spokesperson.campaign.form.button.delete" action="/spokesperson/campaign/delete"/>
		<acme:submit code="spokesperson.campaign.form.button.publish" action="/spokesperson/campaign/publish"/>
	</jstl:if>

	<jstl:if test="${!isCreate}">
		<acme:button code="spokesperson.campaign.form.button.milestones" action="/spokesperson/milestone/list?campaignId=${id}"/>
	</jstl:if>

</acme:form>
