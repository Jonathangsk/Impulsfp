using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IMPULS_Desktop
{
    internal class Candidatos
    
     
    {
        public int applicationId { get; set; }

        public string availability { get; set; }
        public string bio { get; set; }
        public string city { get; set; }
        public string cycle { get; set; }
        public string email { get; set; }
        public string experienceLevel { get; set; }

        public List<string> languages { get; set; }
        public string Name { get; set; }
        public string phoneNumber { get; set; }
        public string portfolio { get; set; }
        public string preferredLocation { get; set; }

        public List<string> preferredRoles { get; set; }
        public List<string> skills { get; set; }

        public string status { get; set; }

        public string surname { get; set; }
        public string username { get; set; }
    }
}

