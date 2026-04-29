using System;
using System.Collections.Generic;

namespace IMPULS_Desktop
{
    internal class Oferta
    {
        public int Id { get; set; }

        public string Title { get; set; }
        public string Description { get; set; }

        public string CompanyName { get; set; }

        public string Location { get; set; }
        public List<string> skills { get; set; } = new List<string>();

        public string Modality { get; set; }

        public string ContractType { get; set; }

//        public decimal Salary { get; set; }
        public decimal? Salary { get; set; }

        //public DateTime? CreationDate { get; set; }
       
        public string CreationDate { get; set; }
        public string State { get; set; }
        public string Cycle { get; set; }


        public int ApplicantsCount { get; set; }
    }
}