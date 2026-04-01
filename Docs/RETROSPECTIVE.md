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
c9a1196ae3145fb3180fbfac6661b3297542548f

# Measurable evidence of improvement

