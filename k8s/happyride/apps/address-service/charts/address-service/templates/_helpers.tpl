{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "address-service.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "address-service.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "address-service.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "address-service.labels" -}}
helm.sh/chart: {{ include "address-service.chart" . }}
{{ include "address-service.selectorLabelsWithDeploymentType" . }}
{{- $appVersion := default .Chart.AppVersion .Values.appVersion }}
{{- if $appVersion }}
app.kubernetes.io/version: {{ $appVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "address-service.selectorLabels" -}}
app.kubernetes.io/name: {{ include "address-service.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Selector labels with deployment type
*/}}
{{- define "address-service.selectorLabelsWithDeploymentType" -}}
{{ include "address-service.selectorLabels" . }}
app.vividcode.io/deployment-type: {{ .Values.deploymentType | quote }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "address-service.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "address-service.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Create the service name with deployment type
*/}}
{{- define "address-service.nameWithDeploymentType" -}}
{{- printf "%s-%s" (include "address-service.name" .) .Values.deploymentType }}
{{- end }}