package com.vitalsense.app.core.data.model

data class HardcodedDoctor(
    val id: String,
    val name: String,
    val specialization: String,
    val contact: String,
    val experienceYears: Int
)

data class Department(
    val id: String,
    val name: String,
    val doctors: List<HardcodedDoctor>
)

object DepartmentDataStore {
    val departments = listOf(
        Department("dept_1", "Cardiology", generateDoctors("Cardiology")),
        Department("dept_2", "Neurology", generateDoctors("Neurology")),
        Department("dept_3", "Orthopedics", generateDoctors("Orthopedics")),
        Department("dept_4", "Pediatrics", generateDoctors("Pediatrics")),
        Department("dept_5", "Oncology", generateDoctors("Oncology")),
        Department("dept_6", "Dermatology", generateDoctors("Dermatology")),
        Department("dept_7", "Gynecology", generateDoctors("Gynecology")),
        Department("dept_8", "General Surgery", generateDoctors("General Surgery"))
    )

    private fun generateDoctors(deptName: String): List<HardcodedDoctor> {
        return (1..5).map { index ->
            val shortName = deptName.split(" ").first()
            HardcodedDoctor(
                id = "${shortName.take(3).lowercase()}_doc_$index",
                name = "Dr. $shortName Specialist $index",
                specialization = "$deptName Specialist",
                contact = "+91 9876543${index.toString().padStart(3, '0')}",
                experienceYears = 5 + (index * 2)
            )
        }
    }
}
