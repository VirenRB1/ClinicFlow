# One meaningful weakness from prior iteration
One of the prior weakness that we had and was solved in this iteration is Email being only
email being treated as a system-wide identity, but is validated as unique only for Patients.
In UserSignupValidator, duplicate checking getPatientByEmail, yet the same validator is reused for 
doctor and staff creation, and later deleteUser / getUserByEmail assume one email maps to one account.
This created design inconsistencies as business layer treated email as a universal key while
persistence contract only enforces it per role table.

# Concrete action taken
In UserSignupValidator, validateDuplicateEmail was changed to validate by UserEmail instead of specific
user ie Patient, Doctor, etc. Now email is used as a system-wide identity and each user is validated through their email.
Updated the repository to support cross role lookup and ensured all user creation paths call the same validator.

Replaced role specific repository calls with generic getUserByEmail. Also introduced type checking to safely cast users.
This helped in removing direct dependency on role specific persistence method thus eliminating incositent lookup behavior across services.
https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g14-booleanhooligans/-/commit/c9a1196ae3145fb3180fbfac6661b3297542548f

# Measurable evidence of improvement
The system previously validated duplicate emails only against patient records, allowing multiple users across different roles to share the same email. 
This created inconsistencies with business logic that assumed email was a unique system-wide identifier. 
After refactoring, duplicate validation now uses a unified repository method ie getUserByEmail, ensuring global uniqueness. 
This eliminated cross role duplication, reduced ambiguous lookup behavior from potentially multiple matches to a single deterministic result,
and removed several classes of bugs related to incorrect retrieval and deletion. Additionally, validator reuse is now correct across all user types, improving both maintainability and testability.

Changed the tests to validate all user by email making it meaningful and uniqueness of email system wide.
https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g14-booleanhooligans/-/commit/13c9a3d814cd8f9b7072445e1a055960e7bba2ea


