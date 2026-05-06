package com.project.backendapi.domain.entity

enum class Category(val displayName: String) {
    CAREER_CONSULTING("진로 상담"),
    RESUME_REVIEW("이력서 첨삭"),
    INTERVIEW_PREP("면접 준비"),
    TECH_STACK("기술 스택"),
    SALARY_NEGOTIATION("연봉 협상"),
    JOB_SEARCH("구직 정보")
}
