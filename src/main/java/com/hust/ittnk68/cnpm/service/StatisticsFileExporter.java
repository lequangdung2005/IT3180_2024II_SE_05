package com.hust.ittnk68.cnpm.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.ittnk68.cnpm.communication.ApiMapping;
import com.hust.ittnk68.cnpm.communication.ServerResponseFile;
import com.hust.ittnk68.cnpm.database.MySQLDatabase;
import com.hust.ittnk68.cnpm.model.*;
import com.hust.ittnk68.cnpm.type.ResponseStatus;
import com.hust.ittnk68.cnpm.type.VehicleType;
import com.opencsv.CSVWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsFileExporter {

	@Autowired
	private MySQLDatabase mysqlDb;

	private Map<String, Object> format(Map<String, Object> input) {
        try {
			input.replaceAll((k, v) -> (v instanceof java.sql.Date) ? v.toString() : v);
			ObjectMapper objectMapper = new ObjectMapper ();
            String json = objectMapper.writeValueAsString(input);
			System.out.println (json);
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
			e.printStackTrace ();
            throw new RuntimeException("Failed to format map", e);
        }
    }

	@GetMapping (ApiMapping.GET_VEHICLE_STATISTICS)
	public ServerResponseFile getVehicleStatistics (@RequestParam("username") String username)
	{
		StringWriter stringWriter = new StringWriter ();
		try (
			CSVWriter csvWriter = new CSVWriter(new PrintWriter(stringWriter))
		) {

			List<Map<String, Object>> families = mysqlDb.findByCondition("1=1", new Family());

			csvWriter.writeNext (new String[]{ "STT", "Family Id", "Số nhà", "Số ô tô", "Số xe máy", "Số xe đạp", "Số xe đạp điện", "Tổng chi phí" });
			int stt = 0;
			for (Map<String, Object> map : families) {

				Map<VehicleType, Integer> vehicleTypeCount = new TreeMap<>();

				Family family = Family.convertFromMap (map);
				// get all vehicle of this family
				List<Map<String, Object>> vehicleListMap = mysqlDb.findByCondition ( String.format("family_id=%d", family.getId()), new Vehicle());
				for (Map<String, Object> vehicleMap : vehicleListMap)
				{
					Vehicle vehicle = Vehicle.convertFromMap (vehicleMap);
					Integer oldCount = (Integer) vehicleTypeCount.getOrDefault(vehicle.getVehicleType(), 0);
					vehicleTypeCount.put (vehicle.getVehicleType(), oldCount + 1);
				}

				int cntCar = (vehicleTypeCount.getOrDefault (VehicleType.CAR, 0));
				int cntMotocycle = (vehicleTypeCount.getOrDefault (VehicleType.MOTORCYCLE, 0));
				int cntBicycle = (vehicleTypeCount.getOrDefault (VehicleType.BICYCLE, 0));
				int cntElecBicyle = (vehicleTypeCount.getOrDefault (VehicleType.ELECTRIC_BICYCLE, 0));
				

				String[] row = new String [8];
				row[0] = String.valueOf (++stt);
				row[1] = String.valueOf (family.getId ());
				row[2] = String.valueOf (family.getHouseNumber ());
				row[3] = String.valueOf (cntCar);
				row[4] = String.valueOf (cntMotocycle);
				row[5] = String.valueOf (cntBicycle);
				row[6] = String.valueOf (cntElecBicyle);
				row[7] = String.valueOf ( 500000 * cntCar + 200000 * cntMotocycle + 50000 * cntBicycle + 100000 * cntElecBicyle );

				csvWriter.writeNext (row);
			}
		}
		catch (Exception e) {
			return new ServerResponseFile (ResponseStatus.INTERNAL_ERROR, e.toString (), null);
		}

		String csvContent = stringWriter.toString ();
		return new ServerResponseFile (ResponseStatus.OK, "success", csvContent);
	}

	@GetMapping (ApiMapping.GET_DONATION_STATISTICS)
	public ServerResponseFile getDonationStatistics (@RequestParam("username") String username)
	{
		StringWriter stringWriter = new StringWriter ();
		try (
			CSVWriter csvWriter = new CSVWriter(new PrintWriter(stringWriter))
		) {

			List<Expense> donations = new ArrayList<>();
			{
				List<Map<String, Object>> donationsList = mysqlDb.findByCondition (
						"expense_type='donation'",
						new Expense ());
				for (Map <String, Object> donationMap : donationsList)
				{
					donations.add (Expense.convertFromMap( format(donationMap) ));
				}
			}

			String[] headers = new String[3 + donations.size()];
			headers[0] = "STT";
			headers[1] = "Family ID";
			headers[2] = "Số nhà";
			for (int i = 0; i < donations.size(); ++i) {
				headers[3 + i] = donations.get (i).getExpenseTitle ();
			}
			csvWriter.writeNext (headers);

			List<Map<String, Object>> families = mysqlDb.findByCondition("1=1", new Family ());

			int stt = 0;
			for (Map<String, Object> map : families) {

				Family family = Family.convertFromMap (map);
				
				String[] row = new String [3 + donations.size ()];
				row[0] = String.valueOf (++stt);
				row[1] = String.valueOf (family.getId ());
				row[2] = String.valueOf (family.getHouseNumber ());
				for (int i = 0; i < donations.size(); ++i) {
					List<Map<String, Object>> pStatusList = mysqlDb.findByCondition(
						String.format ("family_id='%d' AND expense_id='%d'", family.getId(), donations.get(i).getId ()),
						new PaymentStatus ());
					int sum = 0;
					for (Map<String, Object> pStatusMap : pStatusList)
					{
						sum += PaymentStatus.convertFromMap( format (pStatusMap) ).getTotalPay ();
					}
					row[3 + i] = String.valueOf(sum);
				}
				csvWriter.writeNext (row);
			}
		}
		catch (Exception e) {
			return new ServerResponseFile (ResponseStatus.INTERNAL_ERROR, e.toString (), null);
		}

		String csvContent = stringWriter.toString ();
		return new ServerResponseFile (ResponseStatus.OK, "success", csvContent);
	}
	
}
