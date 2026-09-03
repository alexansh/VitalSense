import os
import re

def fix_file(filepath, replacements):
    if not os.path.exists(filepath):
        print(f"Skipping {filepath}, does not exist")
        return
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    for old, new in replacements:
        content = re.sub(old, new, content)
        
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"Fixed {filepath}")

# 1. AdminBroadcastScreen
fix_file(r'app\src\main\java\com\vitalsense\app\feature\admin\AdminBroadcastScreen.kt', [
    (r'Notice\((\s*)id', r'BroadcastNotice(\1id'),
    (r'senderId\s*=\s*"[^"]*",\s*senderRole\s*=\s*"[^"]*"', r'senderRole = com.vitalsense.app.core.data.model.UserRole.ADMIN, senderName = "Admin"'),
    (r'senderRole\s*=\s*com\.vitalsense\.app\.core\.data\.model\.UserRole\.ADMIN,', r'senderRole = com.vitalsense.app.core.data.model.UserRole.ADMIN, senderName = "Admin",'),
    (r'targetRole\s*=\s*targetRole,', r'targetRole = targetRole.name,'),
    (r'targetRole\s*=\s*"[^"]*",', r'targetRole = "ASHA",'),
])

# 2. BroadcastNoticesScreen
fix_file(r'app\src\main\java\com\vitalsense\app\feature\asha\BroadcastNoticesScreen.kt', [
    (r'Notice\((\s*)id', r'BroadcastNotice(\1id'),
    (r'senderId\s*=\s*".*?",', r''),
    (r'senderRole\s*=\s*".*?",', r'senderRole = com.vitalsense.app.core.data.model.UserRole.ASHA, senderName = "Asha",'),
    (r'targetRole\s*=\s*targetRole,', r'targetRole = targetRole.name,'),
])

# 3. VillageListScreen
fix_file(r'app\src\main\java\com\vitalsense\app\feature\admin\VillageListScreen.kt', [
    (r'subDistrict\s*=\s*".*?",', r''),
    (r'assignedAshaIds\s*=\s*listOf\([^)]*\),', r''),
    (r'assignedAshaIds\s*=\s*emptyList\(\),', r''),
    (r'activeOutbreakFlag\s*=\s*(true|false),', r''),
])

# 4. LoginScreen
fix_file(r'app\src\main\java\com\vitalsense\app\feature\auth\LoginScreen.kt', [
    (r'val asha = AshaWorker\([^)]*\)', r'val asha = AshaWorker("demo_asha_1", "Sita Devi", "ASHA-7701", "9988776655", listOf("Rampur", "Shantipur"), 45, 0)'),
    (r'val doctor = Doctor\([^)]*\)', r'val doctor = Doctor("demo_doctor_1", "Dr. Rajesh Verma", "DMC-10293", com.vitalsense.app.core.data.model.DoctorSpecialty.GENERAL_PHYSICIAN, "9988112233", listOf("vil_1", "vil_2"), 0, 15, "dept_gen_med", true)'),
    (r'val patient = Patient\([^)]*\)', r'val patient = Patient("demo_patient_1", "Ramesh Kumar", 45, "Male", "9811100000", "vil_1", "Rampur", "asha_1", "Sita Devi", com.vitalsense.app.core.data.model.SeverityLevel.MODERATE, "Fever", "Today", "Tomorrow", "9811122222", null)'),
    (r'ConditionCategory\.GENERAL_FEVER', r'com.vitalsense.app.core.data.model.ConditionCategory.GENERAL_FEVER'),
])

# 5. DispensaryStockScreen
fix_file(r'app\src\main\java\com\vitalsense\app\feature\doctor\DispensaryStockScreen.kt', [
    (r'reorderLevel\s*=\s*\d+', r'category = "Tablet"'),
    (r'Unresolved reference: Color', r''),
])

# 6. ReferralCreatorScreen
fix_file(r'app\src\main\java\com\vitalsense\app\feature\doctor\ReferralCreatorScreen.kt', [
    (r'icon = Icons\.Default\.ArrowForward', r'icon = { androidx.compose.material3.Icon(Icons.Default.ArrowForward, contentDescription = null) }'),
])

# 7. VitalSenseNavGraph (DispensaryStockScreen import)
fix_file(r'app\src\main\java\com\vitalsense\app\feature\navigation\VitalSenseNavGraph.kt', [
    (r'DispensaryStockScreen\(', r'com.vitalsense.app.feature.doctor.DispensaryStockScreen('),
])
