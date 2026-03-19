<%@page language="java"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:set var="canEdit" value="${_command == 'create' || _command == 'update' || _command == 'publish' || (_command == 'show' && draftMode)}"/>
<jstl:set var="isReadonly" value="${!canEdit}"/>

<acme:form>
    <acme:form-textbox code="sponsor.sponsorship.form.label.ticker" path="ticker" readonly="${isReadonly}"/>
    <acme:form-textbox code="sponsor.sponsorship.form.label.name" path="name" readonly="${isReadonly}"/>
    <acme:form-textarea code="sponsor.sponsorship.form.label.description" path="description" readonly="${isReadonly}"/>

    <acme:form-textbox code="sponsor.sponsorship.form.label.start-moment" path="startMoment"
        placeholder="sponsor.sponsorship.form.placeholder.date" readonly="${isReadonly}"/>

    <acme:form-textbox code="sponsor.sponsorship.form.label.end-moment" path="endMoment"
        placeholder="sponsor.sponsorship.form.placeholder.date" readonly="${isReadonly}"/>

    <jstl:if test="${_command != 'create'}">
        <acme:form-money code="sponsor.sponsorship.form.label.total-money" path="totalMoney" readonly="true"/>
        <acme:form-textbox code="sponsor.sponsorship.form.label.months-active" path="monthsActive" readonly="true"/>
    </jstl:if>

    <acme:form-textbox code="sponsor.sponsorship.form.label.more-info" path="moreInfo" readonly="${isReadonly}"/>

    <jstl:if test="${_command != 'create'}">
        <input type="hidden" name="id" value="${id}"/>
    </jstl:if>

    <hr/>

    <jstl:choose>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="sponsor.sponsorship.form.button.create" action="/sponsor/sponsorship/create"/>
        </jstl:when>
        <jstl:when test="${canEdit}">
            <acme:submit code="sponsor.sponsorship.form.button.update" action="/sponsor/sponsorship/update"/>
            <acme:submit code="sponsor.sponsorship.form.button.delete" action="/sponsor/sponsorship/delete"/>
            <acme:submit code="sponsor.sponsorship.form.button.publish" action="/sponsor/sponsorship/publish"/>
            <acme:button code="sponsor.sponsorship.form.button.donations" action="/sponsor/donation/list?sponsorshipId=${id}"/>
        </jstl:when>
        <jstl:otherwise>
            <acme:button code="sponsor.sponsorship.form.button.view-donations" action="/sponsor/donation/list?sponsorshipId=${id}"/>
        </jstl:otherwise>
    </jstl:choose>

</acme:form>