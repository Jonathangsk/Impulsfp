using System;
using System.Collections.Generic;

namespace IMPULS_Desktop
{
    /// <summary>
    /// Representa un alumne dins del sistema IMPULS.
    /// Conté informació personal, acadèmica i professional.
    /// </summary>
    public class Alumne
    {
        public int Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }

        public string Name { get; set; }
        public string Surname { get; set; }

        public string Email { get; set; }
        public string PhoneNumber { get; set; }
        public string City { get; set; }

        public string Bio { get; set; }
        public string Cycle { get; set; }
        public string ExperienceLevel { get; set; }

        public List<string> Skills { get; set; }
        public List<string> Languages { get; set; }
        public List<string> PreferredRoles { get; set; }

        public string PreferredLocation { get; set; }
        public string Availability { get; set; }

        public string Portfolio { get; set; }
    }
}
