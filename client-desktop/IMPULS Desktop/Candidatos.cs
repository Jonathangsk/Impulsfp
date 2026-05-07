using System.Collections.Generic;
using Newtonsoft.Json;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Representa un candidat dins del sistema IMPULS.
    /// Conté la informació personal, acadèmica i d’estat de l’aplicació.
    /// </summary>
    internal class Candidatos
    {
        public int applicationId { get; set; }

        public string availability { get; set; }
        public string bio { get; set; }
        public string city { get; set; }
        public string cycle { get; set; }
        public string email { get; set; }
        public string experienceLevel { get; set; }

        [JsonProperty("languages")]
        public List<string> languages { get; set; }

        public string name { get; set; }
        public string phoneNumber { get; set; }
        public string portfolio { get; set; }
        public string preferredLocation { get; set; }

        [JsonProperty("preferredRoles")]
        public List<string> preferredRoles { get; set; }

        public List<string> skills { get; set; }

        public string status { get; set; }
        public string surname { get; set; }
        public string username { get; set; }
        public string testResult { get; set; }
        public string languagesText => languages != null ? string.Join(", ", languages) : "";
        public string skillsText => skills != null ? string.Join(", ", skills) : "";
        public string preferredRolesText => preferredRoles != null ? string.Join(", ", preferredRoles) : "";
    }
}